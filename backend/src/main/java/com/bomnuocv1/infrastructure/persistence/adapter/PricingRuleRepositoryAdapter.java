package com.bomnuocv1.infrastructure.persistence.adapter;

import com.bomnuocv1.domain.entity.PricingRule;
import com.bomnuocv1.domain.repository.PricingRuleRepository;
import com.bomnuocv1.domain.valueobject.PricingType;
import com.bomnuocv1.infrastructure.persistence.entity.PricingRuleJpaEntity;
import com.bomnuocv1.infrastructure.persistence.mapper.PricingRulePersistenceMapper;
import com.bomnuocv1.infrastructure.persistence.repository.PricingRuleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PricingRuleRepositoryAdapter implements PricingRuleRepository {

    private final PricingRuleJpaRepository pricingRuleJpaRepository;
    private final PricingRulePersistenceMapper mapper;

    @Override
    public PricingRule save(PricingRule pricingRule) {
        PricingRuleJpaEntity entity = mapper.toEntity(pricingRule);
        PricingRuleJpaEntity saved = pricingRuleJpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<PricingRule> findById(UUID id) {
        return pricingRuleJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<PricingRule> findByOwnerId(UUID ownerId) {
        return pricingRuleJpaRepository.findByOwnerIdOrderByCreatedAtDesc(ownerId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PricingRule> findActiveByOwnerId(UUID ownerId) {
        return pricingRuleJpaRepository.findActiveByOwnerId(ownerId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PricingRule> findActiveByOwnerIdAndUnitLabel(UUID ownerId, String unitLabel) {
        return pricingRuleJpaRepository.findActiveByOwnerIdAndUnitLabel(ownerId, unitLabel).map(mapper::toDomain);
    }

    @Override
    public Optional<PricingRule> findActiveByOwnerIdAndPricingType(UUID ownerId, PricingType pricingType) {
        return pricingRuleJpaRepository.findActiveByOwnerIdAndPricingType(ownerId, pricingType.getCode()).map(mapper::toDomain);
    }

    @Override
    public void delete(PricingRule pricingRule) {
        PricingRuleJpaEntity entity = mapper.toEntity(pricingRule);
        pricingRuleJpaRepository.delete(entity);
    }

    @Override
    public void deleteById(UUID id) {
        pricingRuleJpaRepository.deleteById(id);
    }
}
