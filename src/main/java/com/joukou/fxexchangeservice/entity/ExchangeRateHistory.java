package com.joukou.fxexchangeservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "exchange_rate_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String baseCurrency;
    private String targetCurrency;

    @Column(precision = 19, scale = 6)
    private BigDecimal rate;

    private LocalDate snapshotDate;
    private LocalDateTime createdAt;
}
