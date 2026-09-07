package com.bomnuocv1.application.usecase;

import com.bomnuocv1.application.dto.PaymentResult;
import com.bomnuocv1.application.port.in.RecordPaymentCommand;

public interface RecordPaymentUseCase {

    PaymentResult execute(RecordPaymentCommand command);
}
