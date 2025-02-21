package com.progressoft.FxDealsWarehouse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DealRequestDto(
        @NotBlank(message = "Deal ID is required") String id,

        @NotNull(message = "From currency is required") String fromCurrency,

        @NotNull(message = "To currency is required") String toCurrency,

        @NotNull(message = "Deal amount is required")
        @Positive(message = "Deal amount must be positive") BigDecimal dealAmount) {
}
