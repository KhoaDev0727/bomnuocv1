package com.bomnuocv1.domain.repository;

import com.bomnuocv1.domain.entity.DashboardSummary;

import java.util.UUID;

public interface DashboardRepository {

    DashboardSummary getSummaryByOwnerId(UUID ownerId);
}
