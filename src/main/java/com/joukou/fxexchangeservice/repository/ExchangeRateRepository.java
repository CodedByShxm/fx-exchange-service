package com.joukou.fxexchangeservice.repository;

import com.joukou.fxexchangeservice.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExchangeRateRepository
        extends JpaRepository<ExchangeRate, Long> {

    Optional<ExchangeRate> findByBaseCurrencyAndTargetCurrency(
            String baseCurrency,
            String targetCurrency
    );
}