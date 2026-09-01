package com.bomnuocv1.domain.repository;

import com.bomnuocv1.domain.entity.PumpTransaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PumpTransactionRepository {

    PumpTransaction save(PumpTransaction transaction);

    Optional<PumpTransaction> findById(UUID id);

    List<PumpTransaction> findByOwnerId(UUID ownerId);

    long countTodayPumpsByOwnerId(UUID ownerId, LocalDate today);
}
