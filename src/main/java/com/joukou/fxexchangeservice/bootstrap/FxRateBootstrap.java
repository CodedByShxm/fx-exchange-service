package com.joukou.fxexchangeservice.bootstrap;

import com.joukou.fxexchangeservice.client.ExchangeRateClient;
import com.joukou.fxexchangeservice.dto.LatestRatesResponse;
import com.joukou.fxexchangeservice.entity.ExchangeRate;
import com.joukou.fxexchangeservice.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class FxRateBootstrap {

    private final ExchangeRateRepository repository;
    private final CacheManager cacheManager;
    private final ExchangeRateClient exchangeRateClient;

    @EventListener(ApplicationReadyEvent.class)
    public void warmUpFxRates() {

        log.info("Starting FX bootstrap warm-up...");

        // 1. Check DB first
        long count = repository.count();

        if (count > 0) {
            log.info("Loading FX rates from DB into cache...");

            repository.findAll().forEach(rate -> {
                cacheManager.getCache("fx-rates")
                        .put(rate.getBaseCurrency() + "_" + rate.getTargetCurrency(),
                                rate.getRate());
            });

            log.info("Cache warm-up completed from DB");
            return;
        }

        // 2. If DB empty → fallback to API
        log.warn("DB empty. Fetching FX rates from API...");

        LatestRatesResponse response =
                exchangeRateClient.getLatestRates("USD");

        response.getRates().forEach((currency, rate) -> {

            ExchangeRate entity = ExchangeRate.builder()
                    .baseCurrency("USD")
                    .targetCurrency(currency)
                    .rate(rate.setScale(6, RoundingMode.HALF_UP))
                    .fetchedAt(LocalDateTime.now())
                    .build();

            repository.save(entity);

            cacheManager.getCache("fx-rates")
                    .put("USD_" + currency, entity.getRate());
        });

        log.info("FX bootstrap completed from API");
    }
}