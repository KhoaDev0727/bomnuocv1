package com.bomnuocv1.infrastructure.persistence.repository;

import com.bomnuocv1.infrastructure.persistence.entity.PricingRuleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PricingRuleJpaRepository extends JpaRepository<PricingRuleJpaEntity, UUID> {

    List<PricingRuleJpaEntity> findByOwnerIdOrderByCreatedAtDesc(UUID ownerId);

    @Query("SELECT p FROM PricingRuleJpaEntity p WHERE p.ownerId = :ownerId AND p.effectiveTo IS NULL ORDER BY p.createdAt ASC")
    List<PricingRuleJpaEntity> findActiveByOwnerId(@Param("ownerId") UUID ownerId);

    @Query("SELECT p FROM PricingRuleJpaEntity p WHERE p.ownerId = :ownerId AND p.unitLabel = :unitLabel AND p.effectiveTo IS NULL")
    Optional<PricingRuleJpaEntity> findActiveByOwnerIdAndUnitLabel(@Param("ownerId") UUID ownerId, @Param("unitLabel") String unitLabel);

    @Query("SELECT p FROM PricingRuleJpaEntity p WHERE p.ownerId = :ownerId AND p.pricingType = :pricingType AND p.effectiveTo IS NULL")
    Optional<PricingRuleJpaEntity> findActiveByOwnerIdAndPricingType(@Param("ownerId") UUID ownerId, @Param("pricingType") String pricingType);
}
