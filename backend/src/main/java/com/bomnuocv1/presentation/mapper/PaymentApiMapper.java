package com.bomnuocv1.presentation.mapper;

import com.bomnuocv1.application.dto.PaymentResult;
import com.bomnuocv1.application.port.in.RecordPaymentCommand;
import com.bomnuocv1.presentation.dto.request.RecordPaymentRequest;
import com.bomnuocv1.presentation.dto.response.PaymentResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentApiMapper {

    public RecordPaymentCommand toCommand(RecordPaymentRequest request, UUID ownerId) {
        if (request == null) {
            return null;
        }
        return RecordPaymentCommand.builder()
                .ownerId(ownerId)
                .farmerId(request.getFarmerId())
                .transactionId(request.getTransactionId())
                .amount(request.getAmount())
                .paymentDate(request.getPaymentDate())
                .note(request.getNote())
                .clientUuid(request.getClientUuid())
                .build();
    }

    public PaymentResponse toResponse(PaymentResult result) {
        if (result == null) {
            return null;
        }
        return PaymentResponse.builder()
                .id(result.getId())
                .farmerId(result.getFarmerId())
                .farmerFullName(result.getFarmerFullName())
                .transactionId(result.getTransactionId())
                .amount(result.getAmount())
                .paymentDate(result.getPaymentDate())
                .note(result.getNote())
                .clientUuid(result.getClientUuid())
                .createdAt(result.getCreatedAt())
                .build();
    }
}
