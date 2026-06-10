package com.joukou.fxexchangeservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeResponse {

    private Boolean success;
    private Query query;
    private Info info;
    private String date;
    private BigDecimal result;

    @Data
    public static class Query {
        private String from;
        private String to;
        private BigDecimal amount;
    }

    @Data
    public static class Info {
        private BigDecimal rate;
    }
}
