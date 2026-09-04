package com.bomnuocv1.application.usecase;

import com.bomnuocv1.application.dto.FarmerResult;
import com.bomnuocv1.application.port.in.UpdateFarmerCommand;

public interface UpdateFarmerUseCase {

    FarmerResult execute(UpdateFarmerCommand command);
}
