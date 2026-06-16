package com.joukou.fxexchangeservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class LatestRatesResponse {

    private String base;

    private Map<String, BigDecimal> rates;
}
