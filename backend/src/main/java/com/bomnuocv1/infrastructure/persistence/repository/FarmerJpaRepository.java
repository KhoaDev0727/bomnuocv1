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

    @Query("SELECT f FROM FarmerJpaEntity f WHERE f.ownerId = :ownerId AND f.deleted = false AND (:keyword IS NULL OR :keyword = '' OR LOWER(f.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))) ORDER BY f.fullName ASC")
    List<FarmerJpaEntity> searchActiveByOwnerId(@Param("ownerId") UUID ownerId, @Param("keyword") String keyword);

    @Query("SELECT COUNT(f) > 0 FROM FarmerJpaEntity f WHERE f.ownerId = :ownerId AND f.deleted = false AND f.phoneNumber = :phoneNumber")
    boolean existsActiveByOwnerIdAndPhoneNumber(@Param("ownerId") UUID ownerId, @Param("phoneNumber") String phoneNumber);

    @Query("SELECT COUNT(f) > 0 FROM FarmerJpaEntity f WHERE f.ownerId = :ownerId AND f.deleted = false AND f.phoneNumber = :phoneNumber AND f.id <> :excludeId")
    boolean existsActiveByOwnerIdAndPhoneNumberAndIdNot(@Param("ownerId") UUID ownerId, @Param("phoneNumber") String phoneNumber, @Param("excludeId") UUID excludeId);

    @Query("SELECT COUNT(f) FROM FarmerJpaEntity f WHERE f.ownerId = :ownerId AND f.deleted = false")
    long countActiveByOwnerId(@Param("ownerId") UUID ownerId);
}
