package com.bomnuocv1.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricingRuleResponse {

    private UUID id;
    private UUID ownerId;
    private String pricingType;
    private String pricingTypeDescription;
    private String unitLabel;
    private BigDecimal unitPrice;
    private String formattedUnitPrice;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private boolean active;
    private Instant createdAt;
}
