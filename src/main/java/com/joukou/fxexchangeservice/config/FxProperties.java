package com.joukou.fxexchangeservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "fx")
public class FxProperties {

    private Api api;

    @Data
    public static class Api {
        private String key;
    }
}
