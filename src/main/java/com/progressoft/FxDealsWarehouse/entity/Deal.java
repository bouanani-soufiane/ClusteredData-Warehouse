package com.progressoft.FxDealsWarehouse.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "deals")
public class Deal {

    @Id
    private String id;

    @Column(name = "from_currency", nullable = false, length=3)
    @Size(min = 3, max = 3)
    @NotNull
    private String fromCurrency;

    @Column(name = "to_currency", nullable = false, length=3)
    @Size(min = 3, max = 3)
    @NotNull
    private String toCurrency;

    @PastOrPresent
    @Column(name = "deal_timestamp", nullable = false)
    @NotNull
    private LocalDateTime dealTimestamp;

    @Positive
    @Column(name = "deal_amount", nullable = false)
    @NotNull
    private BigDecimal dealAmount;
}
