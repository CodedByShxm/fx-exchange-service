package com.joukou.fxexchangeservice.repository;

import com.joukou.fxexchangeservice.entity.ExchangeRateHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExchangeRateHistoryRepository
        extends JpaRepository<ExchangeRateHistory, Long> {

    List<ExchangeRateHistory> findByBaseCurrencyAndTargetCurrencyOrderBySnapshotDateDesc(
            String base,
            String target);
}