package com.vn.bomnuocv1.data.repository

import com.vn.bomnuocv1.data.mapper.toDomain
import com.vn.bomnuocv1.data.remote.datasource.DashboardRemoteDataSource
import com.vn.bomnuocv1.domain.model.DashboardSummary
import com.vn.bomnuocv1.domain.repository.DashboardRepository
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val remoteDataSource: DashboardRemoteDataSource
) : DashboardRepository {

    override suspend fun getDashboardSummary(): Result<DashboardSummary> {
        return try {
            val dto = remoteDataSource.getDashboardSummary()
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
