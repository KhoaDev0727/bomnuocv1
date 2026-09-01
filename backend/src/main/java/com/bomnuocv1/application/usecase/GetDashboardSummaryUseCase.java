package com.bomnuocv1.application.usecase;

import com.bomnuocv1.application.dto.DashboardSummaryResult;

import java.util.UUID;

public interface GetDashboardSummaryUseCase {

    DashboardSummaryResult execute(UUID ownerId);
}
