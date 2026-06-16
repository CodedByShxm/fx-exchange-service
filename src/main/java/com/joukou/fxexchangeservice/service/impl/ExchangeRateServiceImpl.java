package com.joukou.fxexchangeservice.service.impl;

import com.joukou.fxexchangeservice.client.ExchangeRateClient;
import com.joukou.fxexchangeservice.entity.ExchangeRate;
import com.joukou.fxexchangeservice.exception.ExternalServiceException;
import com.joukou.fxexchangeservice.repository.ExchangeRateRepository;
import com.joukou.fxexchangeservice.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository repository;
    private final ExchangeRateClient exchangeRateClient;
    private final CacheManager cacheManager;

    private final ConcurrentHashMap<String, Object> locks = new ConcurrentHashMap<>();

    private Object getLock(String key) {
        return locks.computeIfAbsent(key, k -> new Object());
    }

    @Override
    @Cacheable(
            value = "fx-rates",
            key = "#fromCurrency + '_' + #toCurrency")
    public BigDecimal getRate(String fromCurrency, String toCurrency) {

        String key = fromCurrency + "_" + toCurrency;

        log.info("CACHE MISS → DB lookup {}", key);

        // 1. First DB check
        Optional<ExchangeRate> dbRate =
                repository.findByBaseCurrencyAndTargetCurrency(
                        fromCurrency, toCurrency);

        if (dbRate.isPresent()) {
            log.info("DB HIT → {}", key);
            return dbRate.get().getRate();
        }

        // 2. Prevent duplicate API calls (simple in-memory lock)
        synchronized (getLock(key)) {

            // 3. Re-check DB after lock (VERY IMPORTANT)
            dbRate = repository.findByBaseCurrencyAndTargetCurrency(
                    fromCurrency, toCurrency);

            if (dbRate.isPresent()) {
                log.info("DB HIT AFTER LOCK → {}", key);
                return dbRate.get().getRate();
            }

            // 4. API fallback (ONLY ONE THREAD GETS HERE)
            log.warn("DB MISS → calling API for {}", key);

            BigDecimal apiRate =
                    exchangeRateClient.getRate(fromCurrency, toCurrency);

            // 5. Persist (self-healing)
            ExchangeRate entity = ExchangeRate.builder()
                    .baseCurrency(fromCurrency)
                    .targetCurrency(toCurrency)
                    .rate(apiRate)
                    .fetchedAt(LocalDateTime.now())
                    .build();

            repository.save(entity);

            // 6. Warm cache manually
            cacheManager.getCache("fx-rates")
                    .put(key, apiRate);

            return apiRate;
        }
    }
}
