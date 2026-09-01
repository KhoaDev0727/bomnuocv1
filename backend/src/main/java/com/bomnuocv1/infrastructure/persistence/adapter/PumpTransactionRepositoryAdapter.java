package com.bomnuocv1.infrastructure.persistence.adapter;

import com.bomnuocv1.domain.entity.PumpTransaction;
import com.bomnuocv1.domain.repository.PumpTransactionRepository;
import com.bomnuocv1.infrastructure.persistence.entity.PumpTransactionJpaEntity;
import com.bomnuocv1.infrastructure.persistence.mapper.PumpTransactionPersistenceMapper;
import com.bomnuocv1.infrastructure.persistence.repository.PumpTransactionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PumpTransactionRepositoryAdapter implements PumpTransactionRepository {

    private final PumpTransactionJpaRepository pumpTransactionJpaRepository;
    private final PumpTransactionPersistenceMapper mapper;

    @Override
    public PumpTransaction save(PumpTransaction transaction) {
        PumpTransactionJpaEntity entity = mapper.toEntity(transaction);
        PumpTransactionJpaEntity saved = pumpTransactionJpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<PumpTransaction> findById(UUID id) {
        return pumpTransactionJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<PumpTransaction> findByOwnerId(UUID ownerId) {
        return pumpTransactionJpaRepository.findActiveByOwnerId(ownerId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countTodayPumpsByOwnerId(UUID ownerId, LocalDate today) {
        return pumpTransactionJpaRepository.countTodayPumpsByOwnerId(ownerId, today);
    }
}
