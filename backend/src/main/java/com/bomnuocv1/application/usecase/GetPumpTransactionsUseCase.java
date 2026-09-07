package com.bomnuocv1.application.usecase;

import com.bomnuocv1.application.dto.PumpTransactionResult;

import java.util.List;
import java.util.UUID;

public interface GetPumpTransactionsUseCase {

    List<PumpTransactionResult> execute(UUID ownerId, UUID farmerId, String keyword);
}
