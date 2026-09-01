package com.bomnuocv1.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class RecentTransactionResult {

    private final UUID id;
    private final String type;
    private final String farmerName;
    private final String details;
    private final BigDecimal amount;
    private final String formattedAmount;
    private final String statusBadge;
    private final Instant createdAt;
}
