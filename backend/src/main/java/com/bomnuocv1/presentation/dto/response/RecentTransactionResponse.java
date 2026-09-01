package com.bomnuocv1.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentTransactionResponse {

    private UUID id;
    private String type;
    private String farmerName;
    private String details;
    private BigDecimal amount;
    private String formattedAmount;
    private String statusBadge;
    private Instant createdAt;
}
