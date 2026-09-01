package com.vn.bomnuocv1.domain.usecase

import com.vn.bomnuocv1.domain.model.LandUnitOption
import com.vn.bomnuocv1.domain.repository.PricingRepository
import javax.inject.Inject

class GetLandUnitOptionsUseCase @Inject constructor(
    private val repository: PricingRepository
) {
    suspend operator fun invoke(): Result<List<LandUnitOption>> {
        return repository.getLandUnitOptions()
    }
}
