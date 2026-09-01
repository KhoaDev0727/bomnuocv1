package com.vn.bomnuocv1.data.remote.api

import com.vn.bomnuocv1.data.remote.dto.ApiResponseDto
import com.vn.bomnuocv1.data.remote.dto.DashboardSummaryDto
import retrofit2.Response
import retrofit2.http.GET

interface DashboardApiService {

    @GET("api/v1/dashboard/summary")
    suspend fun getDashboardSummary(): Response<ApiResponseDto<DashboardSummaryDto>>
}
