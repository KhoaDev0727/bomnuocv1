package com.bomnuocv1.application.usecase;

import com.bomnuocv1.application.dto.FarmerResult;

import java.util.List;
import java.util.UUID;

public interface GetFarmersUseCase {

    List<FarmerResult> execute(UUID ownerId, String keyword);
}
