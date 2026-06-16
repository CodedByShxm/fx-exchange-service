package com.joukou.fxexchangeservice.client;

import com.joukou.fxexchangeservice.config.FxProperties;
import com.joukou.fxexchangeservice.dto.ExchangeResponse;
import com.joukou.fxexchangeservice.dto.LatestRatesResponse;
import com.joukou.fxexchangeservice.exception.ExternalServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeRateClient {

    private final RestClient restClient;
    private final FxProperties fxProperties;


    @Cacheable(value = "fx-rates", key = "#from + '_' + #to")
    public BigDecimal getRate(String from, String to) {

        try {
            ExchangeResponse response =
                    restClient.get()
                            .uri(uriBuilder -> uriBuilder
                                    .scheme("https")
                                    .host("api.exchangerate.host")
                                    .path("/convert")
                                    .queryParam("access_key", fxProperties.getApi().getKey())
                                    .queryParam("from", from)
                                    .queryParam("to", to)
                                    .queryParam("amount", 1)
                                    .build())
                            .retrieve()
                            .body(ExchangeResponse.class);

            log.info(fxProperties.getApi().getKey());
            log.info(response.toString());
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

    public LatestRatesResponse getLatestRates(String baseCurrency) {

        try {

            log.info("Fetching latest FX rates for base currency {}", baseCurrency);

            LatestRatesResponse response =
                    restClient.get()
                            .uri(uriBuilder -> uriBuilder
                                    .scheme("https")
                                    .host("api.exchangerate.host")
                                    .path("/latest")
                                    .queryParam("base", baseCurrency)
                                    .queryParam("access_key", fxProperties.getApi().getKey())
                                    .build())
                            .retrieve()
                            .body(LatestRatesResponse.class);

            if (response == null || response.getRates() == null) {
                throw new ExternalServiceException(
                        "Invalid response from FX provider");
            }

            return response;

        } catch (Exception ex) {

            log.error("Failed to retrieve FX rates", ex);

            throw new ExternalServiceException(
                    "Unable to retrieve exchange rates");
        }
    }
}