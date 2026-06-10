package com.joukou.fxexchangeservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "conversion_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromCurrency;

    private String toCurrency;

    private BigDecimal amount;

    private BigDecimal exchangeRate;

    private BigDecimal convertedAmount;

    private LocalDateTime createdAt;
}