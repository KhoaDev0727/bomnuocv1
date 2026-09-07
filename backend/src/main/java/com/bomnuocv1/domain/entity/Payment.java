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
    private boolean deleted;
    private final UUID clientUuid;
    private final Instant createdAt;
    private Instant updatedAt;

    public Payment(UUID id, UUID ownerId, UUID farmerId, UUID transactionId, BigDecimal amount, LocalDate paymentDate, String note, boolean deleted, UUID clientUuid, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.ownerId = ownerId;
        this.farmerId = farmerId;
        this.transactionId = transactionId;
        this.amount = amount;
        this.paymentDate = paymentDate != null ? paymentDate : LocalDate.now();
        this.note = note;
        this.deleted = deleted;
        this.clientUuid = clientUuid;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
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
        Instant now = Instant.now();
        return Payment.builder()
                .id(UUID.randomUUID())
                .ownerId(ownerId)
                .farmerId(farmerId)
                .transactionId(transactionId)
                .amount(amount)
                .paymentDate(paymentDate != null ? paymentDate : LocalDate.now())
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
