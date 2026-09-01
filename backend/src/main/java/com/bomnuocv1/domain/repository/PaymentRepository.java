package com.bomnuocv1.domain.repository;

import com.bomnuocv1.domain.entity.Payment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findById(UUID id);

    List<Payment> findByOwnerId(UUID ownerId);
}
