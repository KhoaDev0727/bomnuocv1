package com.bomnuocv1.application.usecase;

import com.bomnuocv1.application.dto.PumpTransactionResult;
import com.bomnuocv1.application.port.in.RecordPumpTransactionCommand;

public interface RecordPumpTransactionUseCase {

    PumpTransactionResult execute(RecordPumpTransactionCommand command);
}
