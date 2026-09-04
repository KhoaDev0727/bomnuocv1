package com.vn.bomnuocv1.domain.usecase

import com.vn.bomnuocv1.domain.model.Farmer
import com.vn.bomnuocv1.domain.repository.FarmerRepository
import javax.inject.Inject

class CreateFarmerUseCase @Inject constructor(
    private val repository: FarmerRepository
) {
    suspend operator fun invoke(
        fullName: String,
        phoneNumber: String?,
        areaNote: String?
    ): Result<Farmer> {
        return repository.createFarmer(fullName, phoneNumber, areaNote)
    }
}
