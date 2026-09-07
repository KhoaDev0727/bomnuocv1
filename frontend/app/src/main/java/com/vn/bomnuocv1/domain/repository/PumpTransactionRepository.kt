package com.vn.bomnuocv1.domain.repository

import com.vn.bomnuocv1.domain.model.PumpTransaction
import java.math.BigDecimal

data class CreatePumpTransactionParams(
    val farmerId: String,
    val pricingRuleId: String?,
    val transactionDate: String,
    val quantity: BigDecimal,
    val quantityUnit: String,
    val unitPrice: BigDecimal,
    val initialPaidAmount: BigDecimal?,
    val note: String?
)

data class UpdatePumpTransactionParams(
    val farmerId: String,
    val pricingRuleId: String?,
    val transactionDate: String,
    val quantity: BigDecimal,
    val quantityUnit: String,
    val unitPrice: BigDecimal,
    val note: String?
)

interface PumpTransactionRepository {

    suspend fun getTransactions(
        farmerId: String? = null,
        keyword: String? = null
    ): Result<List<PumpTransaction>>

    suspend fun createTransaction(
        params: CreatePumpTransactionParams
    ): Result<PumpTransaction>

    suspend fun updateTransaction(
        id: String,
        params: UpdatePumpTransactionParams
    ): Result<PumpTransaction>

    suspend fun deleteTransaction(
        id: String
    ): Result<Unit>
}
