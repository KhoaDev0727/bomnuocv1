package com.bomnuocv1.domain.repository;

import com.bomnuocv1.domain.entity.Farmer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FarmerRepository {

    Farmer save(Farmer farmer);

    Optional<Farmer> findById(UUID id);

    List<Farmer> findByOwnerId(UUID ownerId);

    List<Farmer> searchByOwnerId(UUID ownerId, String keyword);

    boolean existsByOwnerIdAndPhoneNumber(UUID ownerId, String phoneNumber);

    boolean existsByOwnerIdAndPhoneNumberAndIdNot(UUID ownerId, String phoneNumber, UUID excludeId);

    long countByOwnerId(UUID ownerId);
}
