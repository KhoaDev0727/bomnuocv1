package com.vn.bomnuocv1.data.remote.api

import com.vn.bomnuocv1.data.remote.dto.ApiResponseDto
import com.vn.bomnuocv1.data.remote.dto.CreateFarmerRequestDto
import com.vn.bomnuocv1.data.remote.dto.FarmerResponseDto
import com.vn.bomnuocv1.data.remote.dto.UpdateFarmerRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface FarmerApiService {

    @GET("api/v1/farmers")
    suspend fun getFarmers(
        @Query("keyword") keyword: String? = null
    ): Response<ApiResponseDto<List<FarmerResponseDto>>>

    @POST("api/v1/farmers")
    suspend fun createFarmer(
        @Body request: CreateFarmerRequestDto
    ): Response<ApiResponseDto<FarmerResponseDto>>

    @PUT("api/v1/farmers/{id}")
    suspend fun updateFarmer(
        @Path("id") id: String,
        @Body request: UpdateFarmerRequestDto
    ): Response<ApiResponseDto<FarmerResponseDto>>

    @DELETE("api/v1/farmers/{id}")
    suspend fun deleteFarmer(
        @Path("id") id: String
    ): Response<ApiResponseDto<Unit>>
}
