package com.vn.bomnuocv1.domain.repository

import com.vn.bomnuocv1.domain.model.DashboardSummary

interface DashboardRepository {

    suspend fun getDashboardSummary(): Result<DashboardSummary>
}
