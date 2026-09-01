package com.vn.bomnuocv1.data.remote.datasource

import com.vn.bomnuocv1.data.remote.api.DashboardApiService
import com.vn.bomnuocv1.data.remote.dto.DashboardSummaryDto
import javax.inject.Inject

class DashboardRemoteDataSource @Inject constructor(
    private val apiService: DashboardApiService
) {
    suspend fun getDashboardSummary(): DashboardSummaryDto {
        val response = apiService.getDashboardSummary()
        if (response.isSuccessful && response.body()?.data != null) {
            return response.body()!!.data!!
        }
        throw Exception(response.body()?.message ?: "Không thể tải dữ liệu trang chủ.")
    }
}
