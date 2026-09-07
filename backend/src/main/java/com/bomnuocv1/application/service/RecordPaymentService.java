package com.bomnuocv1.application.service;

import com.bomnuocv1.application.dto.PaymentResult;
import com.bomnuocv1.application.port.in.RecordPaymentCommand;
import com.bomnuocv1.application.usecase.RecordPaymentUseCase;
import com.bomnuocv1.domain.entity.Farmer;
import com.bomnuocv1.domain.entity.Payment;
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

@Service
@RequiredArgsConstructor
public class RecordPaymentService implements RecordPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final FarmerRepository farmerRepository;
    private final PumpTransactionRepository pumpTransactionRepository;

    @Override
    @Transactional
    public PaymentResult execute(RecordPaymentCommand command) {
        Farmer farmer = farmerRepository.findById(command.getFarmerId())
                .orElseThrow(() -> new FarmerNotFoundException("Không tìm thấy nông dân."));

        if (!farmer.getOwnerId().equals(command.getOwnerId())) {
            throw new UnauthorizedException("Nông dân không thuộc quyền quản lý của bạn.");
        }

        if (command.getTransactionId() != null) {
            PumpTransaction transaction = pumpTransactionRepository.findById(command.getTransactionId())
                    .orElseThrow(() -> new PumpTransactionNotFoundException("Không tìm thấy giao dịch bơm nước tương ứng."));

            if (!transaction.getOwnerId().equals(command.getOwnerId())) {
                throw new UnauthorizedException("Giao dịch không thuộc quyền quản lý của bạn.");
            }

            if (transaction.isDeleted()) {
                throw new IllegalStateException("Không thể thanh toán cho giao dịch đã bị xóa.");
            }
        }

        Payment payment = Payment.createNew(
                command.getOwnerId(),
                command.getFarmerId(),
                command.getTransactionId(),
                command.getAmount(),
                command.getPaymentDate(),
                command.getNote(),
                command.getClientUuid()
        );

        Payment saved = paymentRepository.save(payment);

        return PaymentResult.builder()
                .id(saved.getId())
                .ownerId(saved.getOwnerId())
                .farmerId(saved.getFarmerId())
                .farmerFullName(farmer.getFullName())
                .transactionId(saved.getTransactionId())
                .amount(saved.getAmount())
                .paymentDate(saved.getPaymentDate())
                .note(saved.getNote())
                .clientUuid(saved.getClientUuid())
                .createdAt(saved.getCreatedAt())
                .build();
    }
}
