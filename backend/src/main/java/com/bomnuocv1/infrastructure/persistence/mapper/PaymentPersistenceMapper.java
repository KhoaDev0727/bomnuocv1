package com.bomnuocv1.infrastructure.persistence.mapper;

import com.bomnuocv1.domain.entity.Payment;
import com.bomnuocv1.infrastructure.persistence.entity.PaymentJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentPersistenceMapper {

    public Payment toDomain(PaymentJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return Payment.builder()
                .id(entity.getId())
                .ownerId(entity.getOwnerId())
                .farmerId(entity.getFarmerId())
                .transactionId(entity.getTransactionId())
                .amount(entity.getAmount())
                .paymentDate(entity.getPaymentDate())
                .note(entity.getNote())
                .clientUuid(entity.getClientUuid())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public PaymentJpaEntity toEntity(Payment domain) {
        if (domain == null) {
            return null;
        }
        return PaymentJpaEntity.builder()
                .id(domain.getId())
                .ownerId(domain.getOwnerId())
                .farmerId(domain.getFarmerId())
                .transactionId(domain.getTransactionId())
                .amount(domain.getAmount())
                .paymentDate(domain.getPaymentDate())
                .note(domain.getNote())
                .clientUuid(domain.getClientUuid())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}
