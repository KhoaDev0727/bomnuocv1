package com.bomnuocv1.application.service;

import com.bomnuocv1.application.dto.PaymentResult;
import com.bomnuocv1.application.usecase.GetPaymentsUseCase;
import com.bomnuocv1.domain.entity.Farmer;
import com.bomnuocv1.domain.entity.Payment;
import com.bomnuocv1.domain.repository.FarmerRepository;
import com.bomnuocv1.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetPaymentsService implements GetPaymentsUseCase {

    private final PaymentRepository paymentRepository;
    private final FarmerRepository farmerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResult> execute(UUID ownerId, UUID farmerId, UUID transactionId) {
        List<Payment> payments;
        if (transactionId != null) {
            payments = paymentRepository.findByTransactionId(transactionId);
        } else if (farmerId != null) {
            payments = paymentRepository.findByFarmerId(farmerId);
        } else {
            payments = paymentRepository.findByOwnerId(ownerId);
        }

        List<Farmer> farmers = farmerRepository.findByOwnerId(ownerId);
        Map<UUID, String> farmerNames = farmers.stream()
                .collect(Collectors.toMap(Farmer::getId, Farmer::getFullName, (f1, f2) -> f1));

        List<PaymentResult> results = new ArrayList<>();
        for (Payment p : payments) {
            if (!p.getOwnerId().equals(ownerId)) {
                continue;
            }
            results.add(PaymentResult.builder()
                    .id(p.getId())
                    .ownerId(p.getOwnerId())
                    .farmerId(p.getFarmerId())
                    .farmerFullName(farmerNames.getOrDefault(p.getFarmerId(), "Nông dân"))
                    .transactionId(p.getTransactionId())
                    .amount(p.getAmount())
                    .paymentDate(p.getPaymentDate())
                    .note(p.getNote())
                    .clientUuid(p.getClientUuid())
                    .createdAt(p.getCreatedAt())
                    .build());
        }

        return results;
    }
}
