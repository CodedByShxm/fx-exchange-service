package com.joukou.fxexchangeservice.service.impl;

import com.joukou.fxexchangeservice.client.ExchangeRateClient;
import com.joukou.fxexchangeservice.dto.*;
import com.joukou.fxexchangeservice.entity.ConversionHistory;
import com.joukou.fxexchangeservice.repository.ConversionHistoryRepository;
import com.joukou.fxexchangeservice.service.CurrencyConversionService;
import com.joukou.fxexchangeservice.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CurrencyConversionServiceImpl implements CurrencyConversionService {

    private final ExchangeRateService exchangeRateService;
    private final ConversionHistoryRepository repository;

    private static final int RATE_SCALE = 6;
    private static final int AMOUNT_SCALE = 2;

    @Override
    public ConversionResponse convert(ConversionRequest request) {

        BigDecimal rate = exchangeRateService.getRate(
                request.getFromCurrency(),
                request.getToCurrency()
        ).setScale(RATE_SCALE, RoundingMode.HALF_UP);


        BigDecimal converted = request.getAmount()
                .multiply(rate)
                .setScale(AMOUNT_SCALE , RoundingMode.HALF_UP);

        ConversionHistory history = ConversionHistory.builder()
                .fromCurrency(request.getFromCurrency())
                .toCurrency(request.getToCurrency())
                .amount(request.getAmount())
                .exchangeRate(rate)
                .convertedAmount(converted)
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(history);

        return ConversionResponse.builder()
                .fromCurrency(request.getFromCurrency())
                .toCurrency(request.getToCurrency())
                .amount(request.getAmount())
                .exchangeRate(rate)
                .convertedAmount(converted)
                .timestamp(LocalDateTime.now())
                .build();
    }
}