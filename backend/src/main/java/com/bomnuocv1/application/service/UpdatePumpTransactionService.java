package com.bomnuocv1.application.service;

import com.bomnuocv1.application.dto.PumpTransactionResult;
import com.bomnuocv1.application.port.in.UpdatePumpTransactionCommand;
import com.bomnuocv1.application.usecase.UpdatePumpTransactionUseCase;
import com.bomnuocv1.domain.entity.Farmer;
import com.bomnuocv1.domain.entity.PumpTransaction;
import com.bomnuocv1.domain.exception.FarmerNotFoundException;
import com.bomnuocv1.domain.exception.PumpTransactionNotFoundException;
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
public class UpdatePumpTransactionService implements UpdatePumpTransactionUseCase {

    private final PumpTransactionRepository pumpTransactionRepository;
    private final PaymentRepository paymentRepository;
    private final FarmerRepository farmerRepository;

    @Override
    @Transactional
    public PumpTransactionResult execute(UpdatePumpTransactionCommand command) {
        PumpTransaction transaction = pumpTransactionRepository.findById(command.getId())
                .orElseThrow(() -> new PumpTransactionNotFoundException("Không tìm thấy giao dịch bơm nước."));

        if (!transaction.getOwnerId().equals(command.getOwnerId())) {
            throw new UnauthorizedException("Bạn không có quyền chỉnh sửa giao dịch này.");
        }

        if (transaction.isDeleted()) {
            throw new IllegalStateException("Giao dịch này đã bị xóa.");
        }

        Farmer farmer = farmerRepository.findById(command.getFarmerId())
                .orElseThrow(() -> new FarmerNotFoundException("Không tìm thấy nông dân."));

        if (!farmer.getOwnerId().equals(command.getOwnerId())) {
            throw new UnauthorizedException("Nông dân không thuộc quyền quản lý của bạn.");
        }

        transaction.updateDetails(
                command.getFarmerId(),
                command.getPricingRuleId(),
                command.getTransactionDate(),
                command.getQuantity(),
                command.getQuantityUnit(),
                command.getUnitPrice(),
                command.getNote()
        );

        PumpTransaction updated = pumpTransactionRepository.save(transaction);

        BigDecimal paidAmount = paymentRepository.sumPaidByTransactionId(updated.getId());
        if (paidAmount == null) {
            paidAmount = BigDecimal.ZERO;
        }

        BigDecimal due = updated.getAmountDue() != null ? updated.getAmountDue() : BigDecimal.ZERO;
        BigDecimal remainingDebt = due.subtract(paidAmount);
        if (remainingDebt.compareTo(BigDecimal.ZERO) < 0) {
            remainingDebt = BigDecimal.ZERO;
        }

        String paymentStatus;
        if (remainingDebt.compareTo(BigDecimal.ZERO) == 0 && due.compareTo(BigDecimal.ZERO) > 0) {
            paymentStatus = "PAID";
        } else if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
            paymentStatus = "PARTIAL";
        } else {
            paymentStatus = "UNPAID";
        }

        return PumpTransactionResult.builder()
                .id(updated.getId())
                .ownerId(updated.getOwnerId())
                .farmerId(updated.getFarmerId())
                .farmerFullName(farmer.getFullName())
                .farmerPhone(farmer.getPhoneNumber())
                .pricingRuleId(updated.getPricingRuleId())
                .transactionDate(updated.getTransactionDate())
                .quantity(updated.getQuantity())
                .quantityUnit(updated.getQuantityUnit())
                .unitPrice(updated.getUnitPrice())
                .amountDue(due)
                .paidAmount(paidAmount)
                .remainingDebt(remainingDebt)
                .paymentStatus(paymentStatus)
                .note(updated.getNote())
                .clientUuid(updated.getClientUuid())
                .createdAt(updated.getCreatedAt())
                .updatedAt(updated.getUpdatedAt())
                .build();
    }
}
