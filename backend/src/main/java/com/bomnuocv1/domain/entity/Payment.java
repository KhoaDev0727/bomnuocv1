package com.bomnuocv1.domain.entity;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class Payment {

    private final UUID id;
    private final UUID ownerId;
    private final UUID farmerId;
    private final UUID transactionId;
    private final BigDecimal amount;
    private final LocalDate paymentDate;
    private final String note;
    private final UUID clientUuid;
    private final Instant createdAt;

    public Payment(UUID id, UUID ownerId, UUID farmerId, UUID transactionId, BigDecimal amount, LocalDate paymentDate, String note, UUID clientUuid, Instant createdAt) {
        this.id = id;
        this.ownerId = ownerId;
        this.farmerId = farmerId;
        this.transactionId = transactionId;
        this.amount = amount;
        this.paymentDate = paymentDate != null ? paymentDate : LocalDate.now();
        this.note = note;
        this.clientUuid = clientUuid;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
    }

    public static Payment createNew(
            UUID ownerId,
            UUID farmerId,
            UUID transactionId,
            BigDecimal amount,
            LocalDate paymentDate,
            String note,
            UUID clientUuid
    ) {
        return Payment.builder()
                .id(UUID.randomUUID())
                .ownerId(ownerId)
                .farmerId(farmerId)
                .transactionId(transactionId)
                .amount(amount)
                .paymentDate(paymentDate != null ? paymentDate : LocalDate.now())
                .note(note)
                .clientUuid(clientUuid)
                .createdAt(Instant.now())
                .build();
    }
}
