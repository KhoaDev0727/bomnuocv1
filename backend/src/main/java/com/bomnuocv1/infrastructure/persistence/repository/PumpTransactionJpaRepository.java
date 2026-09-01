package com.bomnuocv1.infrastructure.persistence.repository;

import com.bomnuocv1.infrastructure.persistence.entity.PumpTransactionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface PumpTransactionJpaRepository extends JpaRepository<PumpTransactionJpaEntity, UUID> {

    @Query("SELECT t FROM PumpTransactionJpaEntity t WHERE t.ownerId = :ownerId AND t.deleted = false ORDER BY t.createdAt DESC")
    List<PumpTransactionJpaEntity> findActiveByOwnerId(@Param("ownerId") UUID ownerId);

    @Query("SELECT COUNT(t) FROM PumpTransactionJpaEntity t WHERE t.ownerId = :ownerId AND t.transactionDate = :today AND t.deleted = false")
    long countTodayPumpsByOwnerId(@Param("ownerId") UUID ownerId, @Param("today") LocalDate today);

    @Query("SELECT COALESCE(SUM(t.amountDue), 0) FROM PumpTransactionJpaEntity t WHERE t.ownerId = :ownerId AND t.deleted = false")
    BigDecimal sumTotalDueByOwnerId(@Param("ownerId") UUID ownerId);

    @Query("SELECT t FROM PumpTransactionJpaEntity t WHERE t.ownerId = :ownerId AND t.deleted = false ORDER BY t.createdAt DESC")
    List<PumpTransactionJpaEntity> findTop10ByOwnerId(@Param("ownerId") UUID ownerId);
}
