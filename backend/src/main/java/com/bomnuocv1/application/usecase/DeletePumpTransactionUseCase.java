package com.bomnuocv1.application.usecase;

import java.util.UUID;

public interface DeletePumpTransactionUseCase {

    void execute(UUID id, UUID ownerId);
}
