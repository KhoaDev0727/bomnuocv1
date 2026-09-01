package com.bomnuocv1.infrastructure.persistence.repository;

import com.bomnuocv1.infrastructure.persistence.entity.FarmerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FarmerJpaRepository extends JpaRepository<FarmerJpaEntity, UUID> {

    @Query("SELECT f FROM FarmerJpaEntity f WHERE f.ownerId = :ownerId AND f.deleted = false ORDER BY f.fullName ASC")
    List<FarmerJpaEntity> findActiveByOwnerId(@Param("ownerId") UUID ownerId);

    @Query("SELECT COUNT(f) FROM FarmerJpaEntity f WHERE f.ownerId = :ownerId AND f.deleted = false")
    long countActiveByOwnerId(@Param("ownerId") UUID ownerId);
}
