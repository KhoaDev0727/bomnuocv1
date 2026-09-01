package com.bomnuocv1.infrastructure.persistence.adapter;

import com.bomnuocv1.domain.entity.Farmer;
import com.bomnuocv1.domain.repository.FarmerRepository;
import com.bomnuocv1.infrastructure.persistence.entity.FarmerJpaEntity;
import com.bomnuocv1.infrastructure.persistence.mapper.FarmerPersistenceMapper;
import com.bomnuocv1.infrastructure.persistence.repository.FarmerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FarmerRepositoryAdapter implements FarmerRepository {

    private final FarmerJpaRepository farmerJpaRepository;
    private final FarmerPersistenceMapper mapper;

    @Override
    public Farmer save(Farmer farmer) {
        FarmerJpaEntity entity = mapper.toEntity(farmer);
        FarmerJpaEntity saved = farmerJpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Farmer> findById(UUID id) {
        return farmerJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Farmer> findByOwnerId(UUID ownerId) {
        return farmerJpaRepository.findActiveByOwnerId(ownerId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countByOwnerId(UUID ownerId) {
        return farmerJpaRepository.countActiveByOwnerId(ownerId);
    }
}
