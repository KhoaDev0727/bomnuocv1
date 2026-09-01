package com.bomnuocv1.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class PricingRuleResult {

    private final UUID id;
    private final UUID ownerId;
    private final String pricingType;
    private final String pricingTypeDescription;
    private final String unitLabel;
    private final BigDecimal unitPrice;
    private final String formattedUnitPrice;
    private final LocalDate effectiveFrom;
    private final LocalDate effectiveTo;
    private final boolean active;
    private final Instant createdAt;
}
