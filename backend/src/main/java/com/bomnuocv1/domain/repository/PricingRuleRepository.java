package com.bomnuocv1.domain.repository;

import com.bomnuocv1.domain.entity.PricingRule;
import com.bomnuocv1.domain.valueobject.PricingType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PricingRuleRepository {

    PricingRule save(PricingRule pricingRule);

    Optional<PricingRule> findById(UUID id);

    List<PricingRule> findByOwnerId(UUID ownerId);

    List<PricingRule> findActiveByOwnerId(UUID ownerId);

    Optional<PricingRule> findActiveByOwnerIdAndUnitLabel(UUID ownerId, String unitLabel);

    Optional<PricingRule> findActiveByOwnerIdAndPricingType(UUID ownerId, PricingType pricingType);
}
