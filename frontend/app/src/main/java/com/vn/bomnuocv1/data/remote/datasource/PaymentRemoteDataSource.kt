package com.vn.bomnuocv1.data.remote.datasource

import com.google.gson.Gson
import com.vn.bomnuocv1.data.remote.api.PaymentApiService
import com.vn.bomnuocv1.data.remote.dto.ApiResponseDto
import com.vn.bomnuocv1.data.remote.dto.PaymentResponseDto
import com.vn.bomnuocv1.data.remote.dto.RecordPaymentRequestDto
import com.vn.bomnuocv1.domain.repository.CreatePaymentParams
import javax.inject.Inject

class PaymentRemoteDataSource @Inject constructor(
    private val apiService: PaymentApiService,
    private val gson: Gson
) {

    suspend fun recordPayment(
        params: CreatePaymentParams
    ): PaymentResponseDto {
        val request = RecordPaymentRequestDto(
            farmerId = params.farmerId,
            transactionId = params.transactionId,
            amount = params.amount,
            paymentDate = params.paymentDate,
            note = params.note
        )
        val response = apiService.recordPayment(request)
        if (response.isSuccessful && response.body()?.data != null) {
            return response.body()!!.data!!
        }
        val errorMsg = parseErrorMessage(response.errorBody()?.string())
            ?: response.body()?.message
            ?: "Không thể ghi nhận thanh toán."
        throw Exception(errorMsg)
    }

    suspend fun getPayments(
        farmerId: String?,
        transactionId: String?
    ): List<PaymentResponseDto> {
        val response = apiService.getPayments(farmerId, transactionId)
        if (response.isSuccessful && response.body()?.data != null) {
            return response.body()!!.data!!
        }
        val errorMsg = parseErrorMessage(response.errorBody()?.string())
            ?: response.body()?.message
            ?: "Không thể tải lịch sử thanh toán."
        throw Exception(errorMsg)
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
