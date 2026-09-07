package com.bomnuocv1.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class PumpTransactionResult {

    private final UUID id;
    private final UUID ownerId;
    private final UUID farmerId;
    private final String farmerFullName;
    private final String farmerPhone;
    private final UUID pricingRuleId;
    private final LocalDate transactionDate;
    private final BigDecimal quantity;
    private final String quantityUnit;
    private final BigDecimal unitPrice;
    private final BigDecimal amountDue;
    private final BigDecimal paidAmount;
    private final BigDecimal remainingDebt;
    private final String paymentStatus;
    private final String note;
    private final UUID clientUuid;
    private final Instant createdAt;
    private final Instant updatedAt;
}
