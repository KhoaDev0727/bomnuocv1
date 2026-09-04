package com.bomnuocv1.application.usecase;

import com.bomnuocv1.application.dto.FarmerResult;
import com.bomnuocv1.application.port.in.CreateFarmerCommand;

public interface CreateFarmerUseCase {

    FarmerResult execute(CreateFarmerCommand command);
}
