package com.bomnuocv1.infrastructure.persistence.mapper;

import com.bomnuocv1.domain.entity.PumpTransaction;
import com.bomnuocv1.infrastructure.persistence.entity.PumpTransactionJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PumpTransactionPersistenceMapper {

    public PumpTransaction toDomain(PumpTransactionJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return PumpTransaction.builder()
                .id(entity.getId())
                .ownerId(entity.getOwnerId())
                .farmerId(entity.getFarmerId())
                .pricingRuleId(entity.getPricingRuleId())
                .transactionDate(entity.getTransactionDate())
                .quantity(entity.getQuantity())
                .quantityUnit(entity.getQuantityUnit())
                .unitPrice(entity.getUnitPrice())
                .amountDue(entity.getAmountDue())
                .note(entity.getNote())
                .deleted(entity.isDeleted())
                .clientUuid(entity.getClientUuid())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public PumpTransactionJpaEntity toEntity(PumpTransaction domain) {
        if (domain == null) {
            return null;
        }
        return PumpTransactionJpaEntity.builder()
                .id(domain.getId())
                .ownerId(domain.getOwnerId())
                .farmerId(domain.getFarmerId())
                .pricingRuleId(domain.getPricingRuleId())
                .transactionDate(domain.getTransactionDate())
                .quantity(domain.getQuantity())
                .quantityUnit(domain.getQuantityUnit())
                .unitPrice(domain.getUnitPrice())
                .amountDue(domain.getAmountDue())
                .note(domain.getNote())
                .deleted(domain.isDeleted())
                .clientUuid(domain.getClientUuid())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
