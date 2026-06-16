package com.joukou.fxexchangeservice.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FxProperties.class)
public class FxConfiguration {
}
