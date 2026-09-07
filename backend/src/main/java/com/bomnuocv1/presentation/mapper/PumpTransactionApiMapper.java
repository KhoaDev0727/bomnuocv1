package com.bomnuocv1.presentation.mapper;

import com.bomnuocv1.application.dto.PumpTransactionResult;
import com.bomnuocv1.application.port.in.RecordPumpTransactionCommand;
import com.bomnuocv1.application.port.in.UpdatePumpTransactionCommand;
import com.bomnuocv1.presentation.dto.request.RecordPumpTransactionRequest;
import com.bomnuocv1.presentation.dto.request.UpdatePumpTransactionRequest;
import com.bomnuocv1.presentation.dto.response.PumpTransactionResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PumpTransactionApiMapper {

    public RecordPumpTransactionCommand toCommand(RecordPumpTransactionRequest request, UUID ownerId) {
        if (request == null) {
            return null;
        }
        return RecordPumpTransactionCommand.builder()
                .ownerId(ownerId)
                .farmerId(request.getFarmerId())
                .pricingRuleId(request.getPricingRuleId())
                .transactionDate(request.getTransactionDate())
                .quantity(request.getQuantity())
                .quantityUnit(request.getQuantityUnit())
                .unitPrice(request.getUnitPrice())
                .initialPaidAmount(request.getInitialPaidAmount())
                .note(request.getNote())
                .clientUuid(request.getClientUuid())
                .build();
    }

    public UpdatePumpTransactionCommand toCommand(UUID id, UpdatePumpTransactionRequest request, UUID ownerId) {
        if (request == null) {
            return null;
        }
        return UpdatePumpTransactionCommand.builder()
                .id(id)
                .ownerId(ownerId)
                .farmerId(request.getFarmerId())
                .pricingRuleId(request.getPricingRuleId())
                .transactionDate(request.getTransactionDate())
                .quantity(request.getQuantity())
                .quantityUnit(request.getQuantityUnit())
                .unitPrice(request.getUnitPrice())
                .note(request.getNote())
                .build();
    }

    public PumpTransactionResponse toResponse(PumpTransactionResult result) {
        if (result == null) {
            return null;
        }
        return PumpTransactionResponse.builder()
                .id(result.getId())
                .farmerId(result.getFarmerId())
                .farmerFullName(result.getFarmerFullName())
                .farmerPhone(result.getFarmerPhone())
                .pricingRuleId(result.getPricingRuleId())
                .transactionDate(result.getTransactionDate())
                .quantity(result.getQuantity())
                .quantityUnit(result.getQuantityUnit())
                .unitPrice(result.getUnitPrice())
                .amountDue(result.getAmountDue())
                .paidAmount(result.getPaidAmount())
                .remainingDebt(result.getRemainingDebt())
                .paymentStatus(result.getPaymentStatus())
                .note(result.getNote())
                .clientUuid(result.getClientUuid())
                .createdAt(result.getCreatedAt())
                .updatedAt(result.getUpdatedAt())
                .build();
    }
}
