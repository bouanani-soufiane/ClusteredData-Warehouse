package com.progressoft.FxDealsWarehouse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DealResponseDto(
        @NotBlank String id,
        @NotNull String fromCurrency,
        @NotNull String toCurrency,
        @NotNull LocalDateTime dealTimestamp,
        @NotNull @Positive BigDecimal dealAmount
) {
}
