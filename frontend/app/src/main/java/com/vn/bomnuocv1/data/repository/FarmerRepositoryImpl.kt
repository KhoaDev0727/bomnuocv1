package com.vn.bomnuocv1.data.repository

import com.vn.bomnuocv1.data.mapper.toDomain
import com.vn.bomnuocv1.data.remote.datasource.FarmerRemoteDataSource
import com.vn.bomnuocv1.domain.model.Farmer
import com.vn.bomnuocv1.domain.repository.FarmerRepository
import javax.inject.Inject

class FarmerRepositoryImpl @Inject constructor(
    private val remoteDataSource: FarmerRemoteDataSource
) : FarmerRepository {

    override suspend fun getFarmers(keyword: String?): Result<List<Farmer>> {
        return try {
            val dtos = remoteDataSource.getFarmers(keyword)
            Result.success(dtos.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createFarmer(
        fullName: String,
        phoneNumber: String?,
        areaNote: String?
    ): Result<Farmer> {
        return try {
            val dto = remoteDataSource.createFarmer(fullName, phoneNumber, areaNote)
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateFarmer(
        id: String,
        fullName: String,
        phoneNumber: String?,
        areaNote: String?
    ): Result<Farmer> {
        return try {
            val dto = remoteDataSource.updateFarmer(id, fullName, phoneNumber, areaNote)
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteFarmer(id: String): Result<Unit> {
        return try {
            remoteDataSource.deleteFarmer(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
