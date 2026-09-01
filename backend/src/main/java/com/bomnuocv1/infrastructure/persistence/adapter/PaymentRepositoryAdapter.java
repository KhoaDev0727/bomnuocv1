package com.bomnuocv1.infrastructure.persistence.adapter;

import com.bomnuocv1.domain.entity.Payment;
import com.bomnuocv1.domain.repository.PaymentRepository;
import com.bomnuocv1.infrastructure.persistence.entity.PaymentJpaEntity;
import com.bomnuocv1.infrastructure.persistence.mapper.PaymentPersistenceMapper;
import com.bomnuocv1.infrastructure.persistence.repository.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;
    private final PaymentPersistenceMapper mapper;

    @Override
    public Payment save(Payment payment) {
        PaymentJpaEntity entity = mapper.toEntity(payment);
        PaymentJpaEntity saved = paymentJpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Payment> findById(UUID id) {
        return paymentJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Payment> findByOwnerId(UUID ownerId) {
        return paymentJpaRepository.findByOwnerId(ownerId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
