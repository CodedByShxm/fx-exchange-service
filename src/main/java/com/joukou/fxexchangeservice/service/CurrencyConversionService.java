package com.joukou.fxexchangeservice.service;

import com.joukou.fxexchangeservice.dto.ConversionRequest;
import com.joukou.fxexchangeservice.dto.ConversionResponse;

public interface CurrencyConversionService {
    ConversionResponse convert(ConversionRequest request);
}
