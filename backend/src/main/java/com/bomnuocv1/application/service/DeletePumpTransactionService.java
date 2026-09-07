package com.bomnuocv1.application.service;

import com.bomnuocv1.application.usecase.DeletePumpTransactionUseCase;
import com.bomnuocv1.domain.entity.Payment;
import com.bomnuocv1.domain.entity.PumpTransaction;
import com.bomnuocv1.domain.exception.PumpTransactionNotFoundException;
import com.bomnuocv1.domain.exception.UnauthorizedException;
import com.bomnuocv1.domain.repository.PaymentRepository;
import com.bomnuocv1.domain.repository.PumpTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeletePumpTransactionService implements DeletePumpTransactionUseCase {

    private final PumpTransactionRepository pumpTransactionRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public void execute(UUID id, UUID ownerId) {
        PumpTransaction transaction = pumpTransactionRepository.findById(id)
                .orElseThrow(() -> new PumpTransactionNotFoundException("Không tìm thấy giao dịch bơm nước cần xóa."));

        if (!transaction.getOwnerId().equals(ownerId)) {
            throw new UnauthorizedException("Bạn không có quyền xóa giao dịch này.");
        }

        // Soft delete transaction
        transaction.markDeleted();
        pumpTransactionRepository.save(transaction);

        // Soft delete all payments linked directly to this transaction
        List<Payment> linkedPayments = paymentRepository.findByTransactionId(id);
        for (Payment payment : linkedPayments) {
            payment.markDeleted();
            paymentRepository.save(payment);
        }
    }
}
