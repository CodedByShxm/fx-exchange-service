package com.joukou.fxexchangeservice.scheduler;

import com.joukou.fxexchangeservice.client.ExchangeRateClient;
import com.joukou.fxexchangeservice.dto.LatestRatesResponse;
import com.joukou.fxexchangeservice.entity.ExchangeRate;
import com.joukou.fxexchangeservice.entity.ExchangeRateHistory;
import com.joukou.fxexchangeservice.repository.ExchangeRateHistoryRepository;
import com.joukou.fxexchangeservice.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class FxRateScheduler{

    private final ExchangeRateClient exchangeRateClient;
    private final ExchangeRateRepository repository;
    private final ExchangeRateHistoryRepository historyRepository;
    private final CacheManager cacheManager;

    @Scheduled(cron = "0 0 0 * * *") // midnight daily
    public void refreshFxRates() {

        log.info("Starting FX snapshot refresh...");

        LatestRatesResponse response =
                exchangeRateClient.getLatestRates("USD");

        if (response == null || response.getRates() == null) {
            log.error("FX API returned invalid response");
            return;
        }

        response.getRates().forEach((currency, rate) -> {

            BigDecimal normalizedRate = rate.setScale(6, RoundingMode.HALF_UP);

            // 1. Save current state
            ExchangeRate entity = repository
                    .findByBaseCurrencyAndTargetCurrency("USD", currency)
                    .orElse(
                            ExchangeRate.builder()
                                    .baseCurrency("USD")
                                    .targetCurrency(currency)
                                    .build()
                    );

            entity.setRate(normalizedRate);
            entity.setFetchedAt(LocalDateTime.now());

            repository.save(entity);

            // 2. Save history (audit trail)
            historyRepository.save(
                    ExchangeRateHistory.builder()
                            .baseCurrency("USD")
                            .targetCurrency(currency)
                            .rate(normalizedRate)
                            .snapshotDate(LocalDate.now())
                            .createdAt(LocalDateTime.now())
                            .build()
            );

            // 3. Detect change
            detectChange("USD", currency, normalizedRate);

            // 4. Update cache safely
            var cache = cacheManager.getCache("fx-rates");
            if (cache != null) {
                cache.put("USD_" + currency, normalizedRate);
            }
        });

        log.info("FX snapshot refresh completed. {} rates updated",
                response.getRates().size());
    }

    private void detectChange(String base, String currency, BigDecimal newRate) {

        var history = historyRepository
                .findByBaseCurrencyAndTargetCurrencyOrderBySnapshotDateDesc(base, currency);

        if (history.size() < 2) return;

        BigDecimal latest = history.get(0).getRate();
        BigDecimal previous = history.get(1).getRate();

        BigDecimal delta = latest.subtract(previous);
        BigDecimal percentChange = previous.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : delta.divide(previous, 6, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        log.info("FX CHANGE {} -> {} | Δ={} | {}%",
                currency, delta, percentChange);
    }
}