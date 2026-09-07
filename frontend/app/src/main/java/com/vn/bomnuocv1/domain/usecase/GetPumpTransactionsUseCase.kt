package com.vn.bomnuocv1.domain.usecase

import com.vn.bomnuocv1.domain.model.PumpTransaction
import com.vn.bomnuocv1.domain.repository.PumpTransactionRepository
import javax.inject.Inject

class GetPumpTransactionsUseCase @Inject constructor(
    private val repository: PumpTransactionRepository
) {
    suspend operator fun invoke(
        farmerId: String? = null,
        keyword: String? = null
    ): Result<List<PumpTransaction>> {
        return repository.getTransactions(farmerId, keyword)
    }
}
