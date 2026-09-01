package com.bomnuocv1.domain.entity;

import com.bomnuocv1.domain.valueobject.PricingType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class PricingRule {

    private final UUID id;
    private final UUID ownerId;
    private final PricingType pricingType;
    private String unitLabel;
    private BigDecimal unitPrice;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private final Instant createdAt;

    public PricingRule(UUID id, UUID ownerId, PricingType pricingType, String unitLabel, BigDecimal unitPrice, LocalDate effectiveFrom, LocalDate effectiveTo, Instant createdAt) {
        this.id = id;
        this.ownerId = ownerId;
        this.pricingType = pricingType;
        this.unitLabel = unitLabel;
        this.unitPrice = unitPrice;
        this.effectiveFrom = effectiveFrom != null ? effectiveFrom : LocalDate.now();
        this.effectiveTo = effectiveTo;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
    }

    public static PricingRule createNew(UUID ownerId, PricingType pricingType, String unitLabel, BigDecimal unitPrice, LocalDate effectiveFrom) {
        return PricingRule.builder()
                .id(UUID.randomUUID())
                .ownerId(ownerId)
                .pricingType(pricingType)
                .unitLabel(unitLabel != null ? unitLabel.trim() : "công")
                .unitPrice(unitPrice != null ? unitPrice : BigDecimal.ZERO)
                .effectiveFrom(effectiveFrom != null ? effectiveFrom : LocalDate.now())
                .effectiveTo(null)
                .createdAt(Instant.now())
                .build();
    }

    public boolean isActive() {
        return effectiveTo == null;
    }

    public void terminate(LocalDate endDate) {
        this.effectiveTo = endDate != null ? endDate : LocalDate.now();
    }

    public void updatePrice(BigDecimal newPrice) {
        if (newPrice != null && newPrice.compareTo(BigDecimal.ZERO) >= 0) {
            this.unitPrice = newPrice;
        }
    }

    public BigDecimal calculateAmountDue(BigDecimal quantity) {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0 || unitPrice == null) {
            return BigDecimal.ZERO;
        }
        return quantity.multiply(unitPrice);
    }
}
