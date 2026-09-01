package com.bomnuocv1.domain.repository;

import com.bomnuocv1.domain.entity.Farmer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FarmerRepository {

    Farmer save(Farmer farmer);

    Optional<Farmer> findById(UUID id);

    List<Farmer> findByOwnerId(UUID ownerId);

    long countByOwnerId(UUID ownerId);
}
