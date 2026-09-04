package com.vn.bomnuocv1.domain.usecase

import com.vn.bomnuocv1.domain.repository.FarmerRepository
import javax.inject.Inject

class DeleteFarmerUseCase @Inject constructor(
    private val repository: FarmerRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return repository.deleteFarmer(id)
    }
}
