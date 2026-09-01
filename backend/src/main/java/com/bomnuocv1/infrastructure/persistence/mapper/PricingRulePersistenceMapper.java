package com.bomnuocv1.infrastructure.persistence.mapper;

import com.bomnuocv1.domain.entity.PricingRule;
import com.bomnuocv1.domain.valueobject.PricingType;
import com.bomnuocv1.infrastructure.persistence.entity.PricingRuleJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PricingRulePersistenceMapper {

    public PricingRule toDomain(PricingRuleJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return PricingRule.builder()
                .id(entity.getId())
                .ownerId(entity.getOwnerId())
                .pricingType(PricingType.fromCode(entity.getPricingType()))
                .unitLabel(entity.getUnitLabel())
                .unitPrice(entity.getUnitPrice())
                .effectiveFrom(entity.getEffectiveFrom())
                .effectiveTo(entity.getEffectiveTo())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public PricingRuleJpaEntity toEntity(PricingRule domain) {
        if (domain == null) {
            return null;
        }
        return PricingRuleJpaEntity.builder()
                .id(domain.getId())
                .ownerId(domain.getOwnerId())
                .pricingType(domain.getPricingType().getCode())
                .unitLabel(domain.getUnitLabel())
                .unitPrice(domain.getUnitPrice())
                .effectiveFrom(domain.getEffectiveFrom())
                .effectiveTo(domain.getEffectiveTo())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}
