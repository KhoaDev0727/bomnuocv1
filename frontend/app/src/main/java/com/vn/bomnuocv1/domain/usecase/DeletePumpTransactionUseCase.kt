package com.vn.bomnuocv1.domain.usecase

import com.vn.bomnuocv1.domain.repository.PumpTransactionRepository
import javax.inject.Inject

class DeletePumpTransactionUseCase @Inject constructor(
    private val repository: PumpTransactionRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return repository.deleteTransaction(id)
    }
}
