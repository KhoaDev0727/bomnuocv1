package com.bomnuocv1.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class PaymentResult {

    private final UUID id;
    private final UUID ownerId;
    private final UUID farmerId;
    private final String farmerFullName;
    private final UUID transactionId;
    private final BigDecimal amount;
    private final LocalDate paymentDate;
    private final String note;
    private final UUID clientUuid;
    private final Instant createdAt;
}
