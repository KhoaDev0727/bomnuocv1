package com.vn.bomnuocv1.data.remote.datasource

import com.google.gson.Gson
import com.vn.bomnuocv1.data.remote.api.PumpTransactionApiService
import com.vn.bomnuocv1.data.remote.dto.ApiResponseDto
import com.vn.bomnuocv1.data.remote.dto.PumpTransactionResponseDto
import com.vn.bomnuocv1.data.remote.dto.RecordPumpTransactionRequestDto
import com.vn.bomnuocv1.data.remote.dto.UpdatePumpTransactionRequestDto
import com.vn.bomnuocv1.domain.repository.CreatePumpTransactionParams
import com.vn.bomnuocv1.domain.repository.UpdatePumpTransactionParams
import javax.inject.Inject

class PumpTransactionRemoteDataSource @Inject constructor(
    private val apiService: PumpTransactionApiService,
    private val gson: Gson
) {

    suspend fun getTransactions(
        farmerId: String?,
        keyword: String?
    ): List<PumpTransactionResponseDto> {
        val response = apiService.getTransactions(farmerId, keyword)
        if (response.isSuccessful && response.body()?.data != null) {
            return response.body()!!.data!!
        }
        val errorMsg = parseErrorMessage(response.errorBody()?.string())
            ?: response.body()?.message
            ?: "Không thể tải danh sách lượt bơm."
        throw Exception(errorMsg)
    }

    suspend fun createTransaction(
        params: CreatePumpTransactionParams
    ): PumpTransactionResponseDto {
        val request = RecordPumpTransactionRequestDto(
            farmerId = params.farmerId,
            pricingRuleId = params.pricingRuleId,
            transactionDate = params.transactionDate,
            quantity = params.quantity,
            quantityUnit = params.quantityUnit,
            unitPrice = params.unitPrice,
            initialPaidAmount = params.initialPaidAmount,
            note = params.note
        )
        val response = apiService.recordTransaction(request)
        if (response.isSuccessful && response.body()?.data != null) {
            return response.body()!!.data!!
        }
        val errorMsg = parseErrorMessage(response.errorBody()?.string())
            ?: response.body()?.message
            ?: "Không thể ghi nhận lượt bơm mới."
        throw Exception(errorMsg)
    }

    suspend fun updateTransaction(
        id: String,
        params: UpdatePumpTransactionParams
    ): PumpTransactionResponseDto {
        val request = UpdatePumpTransactionRequestDto(
            farmerId = params.farmerId,
            pricingRuleId = params.pricingRuleId,
            transactionDate = params.transactionDate,
            quantity = params.quantity,
            quantityUnit = params.quantityUnit,
            unitPrice = params.unitPrice,
            note = params.note
        )
        val response = apiService.updateTransaction(id, request)
        if (response.isSuccessful && response.body()?.data != null) {
            return response.body()!!.data!!
        }
        val errorMsg = parseErrorMessage(response.errorBody()?.string())
            ?: response.body()?.message
            ?: "Không thể cập nhật thông tin lượt bơm."
        throw Exception(errorMsg)
    }

    suspend fun deleteTransaction(id: String) {
        val response = apiService.deleteTransaction(id)
        if (!response.isSuccessful) {
            val errorMsg = parseErrorMessage(response.errorBody()?.string())
                ?: response.body()?.message
                ?: "Không thể xóa lượt bơm."
            throw Exception(errorMsg)
        }
    }

    private fun parseErrorMessage(errorJson: String?): String? {
        if (errorJson.isNullOrBlank()) return null
        return try {
            val parsed = gson.fromJson(errorJson, ApiResponseDto::class.java)
            parsed.message
        } catch (_: Exception) {
            null
        }
    }
}
