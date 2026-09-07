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
public class PumpTransactionResponse {

    private UUID id;
    private UUID farmerId;
    private String farmerFullName;
    private String farmerPhone;
    private UUID pricingRuleId;
    private LocalDate transactionDate;
    private BigDecimal quantity;
    private String quantityUnit;
    private BigDecimal unitPrice;
    private BigDecimal amountDue;
    private BigDecimal paidAmount;
    private BigDecimal remainingDebt;
    private String paymentStatus;
    private String note;
    private UUID clientUuid;
    private Instant createdAt;
    private Instant updatedAt;
}
