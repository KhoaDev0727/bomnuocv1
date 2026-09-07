package com.bomnuocv1.application.service;

import com.bomnuocv1.application.dto.PumpTransactionResult;
import com.bomnuocv1.application.usecase.GetPumpTransactionsUseCase;
import com.bomnuocv1.domain.entity.Farmer;
import com.bomnuocv1.domain.entity.PumpTransaction;
import com.bomnuocv1.domain.repository.FarmerRepository;
import com.bomnuocv1.domain.repository.PaymentRepository;
import com.bomnuocv1.domain.repository.PumpTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetPumpTransactionsService implements GetPumpTransactionsUseCase {

    private final PumpTransactionRepository pumpTransactionRepository;
    private final PaymentRepository paymentRepository;
    private final FarmerRepository farmerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PumpTransactionResult> execute(UUID ownerId, UUID farmerId, String keyword) {
        List<PumpTransaction> transactions;
        if (farmerId != null) {
            transactions = pumpTransactionRepository.findByOwnerIdAndFarmerId(ownerId, farmerId);
        } else {
            transactions = pumpTransactionRepository.findByOwnerId(ownerId);
        }

        List<Farmer> farmers = farmerRepository.findByOwnerId(ownerId);
        Map<UUID, Farmer> farmerMap = farmers.stream()
                .collect(Collectors.toMap(Farmer::getId, f -> f, (f1, f2) -> f1));

        String normalizedKeyword = (keyword != null && !keyword.trim().isEmpty())
                ? keyword.trim().toLowerCase()
                : null;

        List<PumpTransactionResult> results = new ArrayList<>();

        for (PumpTransaction t : transactions) {
            Farmer farmer = farmerMap.get(t.getFarmerId());
            String farmerName = farmer != null ? farmer.getFullName() : "Nông dân";
            String farmerPhone = farmer != null ? farmer.getPhoneNumber() : null;

            if (normalizedKeyword != null) {
                boolean matchName = farmerName.toLowerCase().contains(normalizedKeyword);
                boolean matchPhone = farmerPhone != null && farmerPhone.toLowerCase().contains(normalizedKeyword);
                if (!matchName && !matchPhone) {
                    continue;
                }
            }

            BigDecimal paidAmount = paymentRepository.sumPaidByTransactionId(t.getId());
            if (paidAmount == null) {
                paidAmount = BigDecimal.ZERO;
            }

            BigDecimal due = t.getAmountDue() != null ? t.getAmountDue() : BigDecimal.ZERO;
            BigDecimal remainingDebt = due.subtract(paidAmount);
            if (remainingDebt.compareTo(BigDecimal.ZERO) < 0) {
                remainingDebt = BigDecimal.ZERO;
            }

            String status;
            if (remainingDebt.compareTo(BigDecimal.ZERO) == 0 && due.compareTo(BigDecimal.ZERO) > 0) {
                status = "PAID";
            } else if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
                status = "PARTIAL";
            } else {
                status = "UNPAID";
            }

            results.add(PumpTransactionResult.builder()
                    .id(t.getId())
                    .ownerId(t.getOwnerId())
                    .farmerId(t.getFarmerId())
                    .farmerFullName(farmerName)
                    .farmerPhone(farmerPhone)
                    .pricingRuleId(t.getPricingRuleId())
                    .transactionDate(t.getTransactionDate())
                    .quantity(t.getQuantity())
                    .quantityUnit(t.getQuantityUnit())
                    .unitPrice(t.getUnitPrice())
                    .amountDue(due)
                    .paidAmount(paidAmount)
                    .remainingDebt(remainingDebt)
                    .paymentStatus(status)
                    .note(t.getNote())
                    .clientUuid(t.getClientUuid())
                    .createdAt(t.getCreatedAt())
                    .updatedAt(t.getUpdatedAt())
                    .build());
        }

        return results;
    }
}
