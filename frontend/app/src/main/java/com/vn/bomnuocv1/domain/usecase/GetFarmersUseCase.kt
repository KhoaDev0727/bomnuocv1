package com.vn.bomnuocv1.domain.usecase

import com.vn.bomnuocv1.domain.model.Farmer
import com.vn.bomnuocv1.domain.repository.FarmerRepository
import javax.inject.Inject

class GetFarmersUseCase @Inject constructor(
    private val repository: FarmerRepository
) {
    suspend operator fun invoke(keyword: String? = null): Result<List<Farmer>> {
        return repository.getFarmers(keyword)
    }
}
