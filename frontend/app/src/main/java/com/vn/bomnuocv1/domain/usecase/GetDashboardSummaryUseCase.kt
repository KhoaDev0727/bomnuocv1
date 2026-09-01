package com.vn.bomnuocv1.domain.usecase

import com.vn.bomnuocv1.domain.model.DashboardSummary
import com.vn.bomnuocv1.domain.repository.DashboardRepository
import javax.inject.Inject

class GetDashboardSummaryUseCase @Inject constructor(
    private val repository: DashboardRepository
) {
    suspend operator fun invoke(): Result<DashboardSummary> {
        return repository.getDashboardSummary()
    }
}
