package com.bomnuocv1.application.usecase;

import com.bomnuocv1.application.dto.PumpTransactionResult;
import com.bomnuocv1.application.port.in.UpdatePumpTransactionCommand;

public interface UpdatePumpTransactionUseCase {

    PumpTransactionResult execute(UpdatePumpTransactionCommand command);
}
