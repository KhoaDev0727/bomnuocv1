package com.vn.bomnuocv1.domain.usecase

import com.vn.bomnuocv1.domain.model.Farmer
import com.vn.bomnuocv1.domain.repository.FarmerRepository
import javax.inject.Inject

class UpdateFarmerUseCase @Inject constructor(
    private val repository: FarmerRepository
) {
    suspend operator fun invoke(
        id: String,
        fullName: String,
        phoneNumber: String?,
        areaNote: String?
    ): Result<Farmer> {
        return repository.updateFarmer(id, fullName, phoneNumber, areaNote)
    }
}
