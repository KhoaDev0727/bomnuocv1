package com.vn.bomnuocv1.data.remote.api

import com.vn.bomnuocv1.data.remote.dto.ApiResponseDto
import com.vn.bomnuocv1.data.remote.dto.PaymentResponseDto
import com.vn.bomnuocv1.data.remote.dto.RecordPaymentRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PaymentApiService {

    @POST("api/v1/payments")
    suspend fun recordPayment(
        @Body request: RecordPaymentRequestDto
    ): Response<ApiResponseDto<PaymentResponseDto>>

    @GET("api/v1/payments")
    suspend fun getPayments(
        @Query("farmerId") farmerId: String? = null,
        @Query("transactionId") transactionId: String? = null
    ): Response<ApiResponseDto<List<PaymentResponseDto>>>
}
