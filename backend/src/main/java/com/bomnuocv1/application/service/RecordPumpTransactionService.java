package com.bomnuocv1.application.service;

import com.bomnuocv1.application.dto.PumpTransactionResult;
import com.bomnuocv1.application.port.in.RecordPumpTransactionCommand;
import com.bomnuocv1.application.usecase.RecordPumpTransactionUseCase;
import com.bomnuocv1.domain.entity.Farmer;
import com.bomnuocv1.domain.entity.Payment;
import com.bomnuocv1.domain.entity.PumpTransaction;
import com.bomnuocv1.domain.exception.FarmerNotFoundException;
import com.bomnuocv1.domain.exception.UnauthorizedException;
import com.bomnuocv1.domain.repository.FarmerRepository;
import com.bomnuocv1.domain.repository.PaymentRepository;
import com.bomnuocv1.domain.repository.PumpTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RecordPumpTransactionService implements RecordPumpTransactionUseCase {

    private final PumpTransactionRepository pumpTransactionRepository;
    private final PaymentRepository paymentRepository;
    private final FarmerRepository farmerRepository;

    @Override
    @Transactional
    public PumpTransactionResult execute(RecordPumpTransactionCommand command) {
        Farmer farmer = farmerRepository.findById(command.getFarmerId())
                .orElseThrow(() -> new FarmerNotFoundException("Không tìm thấy thông tin nông dân."));

        if (!farmer.getOwnerId().equals(command.getOwnerId())) {
            throw new UnauthorizedException("Bạn không có quyền thao tác trên nông dân này.");
        }

        PumpTransaction transaction = PumpTransaction.createNew(
                command.getOwnerId(),
                command.getFarmerId(),
                command.getPricingRuleId(),
                command.getTransactionDate(),
                command.getQuantity(),
                command.getQuantityUnit(),
                command.getUnitPrice(),
                command.getNote(),
                command.getClientUuid()
        );

        PumpTransaction savedTransaction = pumpTransactionRepository.save(transaction);

        BigDecimal paidAmount = BigDecimal.ZERO;
        BigDecimal initialPaid = command.getInitialPaidAmount();
        if (initialPaid != null && initialPaid.compareTo(BigDecimal.ZERO) > 0) {
            Payment payment = Payment.createNew(
                    command.getOwnerId(),
                    command.getFarmerId(),
                    savedTransaction.getId(),
                    initialPaid,
                    command.getTransactionDate(),
                    "Thanh toán lúc bơm",
                    command.getClientUuid()
            );
            paymentRepository.save(payment);
            paidAmount = initialPaid;
        }

        BigDecimal remainingDebt = savedTransaction.getAmountDue().subtract(paidAmount);
        if (remainingDebt.compareTo(BigDecimal.ZERO) < 0) {
            remainingDebt = BigDecimal.ZERO;
        }

        String paymentStatus;
        if (remainingDebt.compareTo(BigDecimal.ZERO) == 0 && savedTransaction.getAmountDue().compareTo(BigDecimal.ZERO) > 0) {
            paymentStatus = "PAID";
        } else if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
            paymentStatus = "PARTIAL";
        } else {
            paymentStatus = "UNPAID";
        }

        return PumpTransactionResult.builder()
                .id(savedTransaction.getId())
                .ownerId(savedTransaction.getOwnerId())
                .farmerId(savedTransaction.getFarmerId())
                .farmerFullName(farmer.getFullName())
                .farmerPhone(farmer.getPhoneNumber())
                .pricingRuleId(savedTransaction.getPricingRuleId())
                .transactionDate(savedTransaction.getTransactionDate())
                .quantity(savedTransaction.getQuantity())
                .quantityUnit(savedTransaction.getQuantityUnit())
                .unitPrice(savedTransaction.getUnitPrice())
                .amountDue(savedTransaction.getAmountDue())
                .paidAmount(paidAmount)
                .remainingDebt(remainingDebt)
                .paymentStatus(paymentStatus)
                .note(savedTransaction.getNote())
                .clientUuid(savedTransaction.getClientUuid())
                .createdAt(savedTransaction.getCreatedAt())
                .updatedAt(savedTransaction.getUpdatedAt())
                .build();
    }
}
