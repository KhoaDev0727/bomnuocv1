package com.bomnuocv1.domain.entity;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class PumpTransaction {

    private final UUID id;
    private final UUID ownerId;
    private final UUID farmerId;
    private final UUID pricingRuleId;
    private final LocalDate transactionDate;
    private final BigDecimal quantity;
    private final String quantityUnit;
    private final BigDecimal unitPrice;
    private final BigDecimal amountDue;
    private final String note;
    private boolean deleted;
    private final UUID clientUuid;
    private final Instant createdAt;
    private Instant updatedAt;

    public PumpTransaction(UUID id, UUID ownerId, UUID farmerId, UUID pricingRuleId, LocalDate transactionDate, BigDecimal quantity, String quantityUnit, BigDecimal unitPrice, BigDecimal amountDue, String note, boolean deleted, UUID clientUuid, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.ownerId = ownerId;
        this.farmerId = farmerId;
        this.pricingRuleId = pricingRuleId;
        this.transactionDate = transactionDate != null ? transactionDate : LocalDate.now();
        this.quantity = quantity;
        this.quantityUnit = quantityUnit;
        this.unitPrice = unitPrice;
        this.amountDue = amountDue != null ? amountDue : (quantity != null && unitPrice != null ? quantity.multiply(unitPrice) : BigDecimal.ZERO);
        this.note = note;
        this.deleted = deleted;
        this.clientUuid = clientUuid;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
    }

    public static PumpTransaction createNew(
            UUID ownerId,
            UUID farmerId,
            UUID pricingRuleId,
            LocalDate transactionDate,
            BigDecimal quantity,
            String quantityUnit,
            BigDecimal unitPrice,
            String note,
            UUID clientUuid
    ) {
        Instant now = Instant.now();
        BigDecimal due = (quantity != null && unitPrice != null) ? quantity.multiply(unitPrice) : BigDecimal.ZERO;
        return PumpTransaction.builder()
                .id(UUID.randomUUID())
                .ownerId(ownerId)
                .farmerId(farmerId)
                .pricingRuleId(pricingRuleId)
                .transactionDate(transactionDate != null ? transactionDate : LocalDate.now())
                .quantity(quantity)
                .quantityUnit(quantityUnit)
                .unitPrice(unitPrice)
                .amountDue(due)
                .note(note)
                .deleted(false)
                .clientUuid(clientUuid)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public void markDeleted() {
        this.deleted = true;
        this.updatedAt = Instant.now();
    }
}
