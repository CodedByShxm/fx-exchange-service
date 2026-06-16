package com.joukou.fxexchangeservice.service;

import java.math.BigDecimal;

public interface ExchangeRateService {

    BigDecimal getRate(
            String fromCurrency,
            String toCurrency);
}
