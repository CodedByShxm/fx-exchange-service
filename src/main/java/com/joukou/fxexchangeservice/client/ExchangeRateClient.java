package com.joukou.fxexchangeservice.client;

import com.joukou.fxexchangeservice.dto.ExchangeResponse;
import com.joukou.fxexchangeservice.exception.ExternalServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ExchangeRateClient {

    private final RestClient restClient;

    @Value("${fx.api.key}")
    private String apiKey;

    @Cacheable(value = "fx-rates", key = "#from + '_' + #to")
    public BigDecimal getRate(String from, String to) {

        try {
            ExchangeResponse response =
                    restClient.get()
                            .uri(uriBuilder -> uriBuilder
                                    .scheme("https")
                                    .host("api.exchangerate.host")
                                    .path("/convert")
                                    .queryParam("access_key", apiKey)
                                    .queryParam("from", from)
                                    .queryParam("to", to)
                                    .queryParam("amount", 1)
                                    .build())
                            .retrieve()
                            .body(ExchangeResponse.class);

            if (response == null || response.getResult() == null) {
                throw new ExternalServiceException("Invalid FX response from provider");
            }

            return response.getResult();

        } catch (Exception ex) {
            throw new ExternalServiceException(
                    "Failed to fetch exchange rate: " + ex.getMessage()
            );
        }
    }
}