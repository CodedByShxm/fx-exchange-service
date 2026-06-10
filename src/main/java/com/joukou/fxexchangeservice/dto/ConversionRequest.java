package com.joukou.fxexchangeservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConversionRequest {

    @NotBlank(message = "fromCurrency is required")
    @Size(min = 3, max = 3, message = "currency must be 3 letters")
    private String fromCurrency;

    @NotBlank
    @Size(min = 3, max = 3)
    private String toCurrency;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;
}
