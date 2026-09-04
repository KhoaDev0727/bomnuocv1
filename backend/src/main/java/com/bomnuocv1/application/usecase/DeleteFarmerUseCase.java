package com.bomnuocv1.application.usecase;

import java.util.UUID;

public interface DeleteFarmerUseCase {

    void execute(UUID farmerId, UUID ownerId);
}
