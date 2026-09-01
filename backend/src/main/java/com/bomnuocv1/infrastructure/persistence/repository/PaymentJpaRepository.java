package com.bomnuocv1.infrastructure.persistence.repository;

import com.bomnuocv1.infrastructure.persistence.entity.PaymentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentJpaEntity, UUID> {

    @Query("SELECT p FROM PaymentJpaEntity p WHERE p.ownerId = :ownerId ORDER BY p.createdAt DESC")
    List<PaymentJpaEntity> findByOwnerId(@Param("ownerId") UUID ownerId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM PaymentJpaEntity p WHERE p.ownerId = :ownerId")
    BigDecimal sumTotalPaidByOwnerId(@Param("ownerId") UUID ownerId);

    @Query("SELECT p FROM PaymentJpaEntity p WHERE p.ownerId = :ownerId ORDER BY p.createdAt DESC")
    List<PaymentJpaEntity> findTop10ByOwnerId(@Param("ownerId") UUID ownerId);
}
