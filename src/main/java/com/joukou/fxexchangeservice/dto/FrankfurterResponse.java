package com.joukou.fxexchangeservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class FrankfurterResponse {

    private String base;
    private Map<String, BigDecimal> rates;
}
