package com.bomnuocv1.domain.repository;

import com.bomnuocv1.domain.entity.Payment;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findById(UUID id);

    List<Payment> findByOwnerId(UUID ownerId);

    List<Payment> findByTransactionId(UUID transactionId);

    List<Payment> findByFarmerId(UUID farmerId);

    BigDecimal sumPaidByTransactionId(UUID transactionId);
}
