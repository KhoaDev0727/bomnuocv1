package com.vn.bomnuocv1.data.remote.api

import com.vn.bomnuocv1.data.remote.dto.ApiResponseDto
import com.vn.bomnuocv1.data.remote.dto.PumpTransactionResponseDto
import com.vn.bomnuocv1.data.remote.dto.RecordPumpTransactionRequestDto
import com.vn.bomnuocv1.data.remote.dto.UpdatePumpTransactionRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PumpTransactionApiService {

    @GET("api/v1/pump-transactions")
    suspend fun getTransactions(
        @Query("farmerId") farmerId: String? = null,
        @Query("keyword") keyword: String? = null
    ): Response<ApiResponseDto<List<PumpTransactionResponseDto>>>

    @POST("api/v1/pump-transactions")
    suspend fun recordTransaction(
        @Body request: RecordPumpTransactionRequestDto
    ): Response<ApiResponseDto<PumpTransactionResponseDto>>

    @PUT("api/v1/pump-transactions/{id}")
    suspend fun updateTransaction(
        @Path("id") id: String,
        @Body request: UpdatePumpTransactionRequestDto
    ): Response<ApiResponseDto<PumpTransactionResponseDto>>

    @DELETE("api/v1/pump-transactions/{id}")
    suspend fun deleteTransaction(
        @Path("id") id: String
    ): Response<ApiResponseDto<Unit>>
}
