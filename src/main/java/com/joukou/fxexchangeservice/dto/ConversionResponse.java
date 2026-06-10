package com.joukou.fxexchangeservice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ConversionResponse {

    private String fromCurrency;
    private String toCurrency;
    private BigDecimal amount;

    private BigDecimal exchangeRate;
    private BigDecimal convertedAmount;

    private LocalDateTime timestamp;
}
