package com.bomnuocv1.application.usecase;

import com.bomnuocv1.application.dto.PaymentResult;

import java.util.List;
import java.util.UUID;

public interface GetPaymentsUseCase {

    List<PaymentResult> execute(UUID ownerId, UUID farmerId, UUID transactionId);
}
