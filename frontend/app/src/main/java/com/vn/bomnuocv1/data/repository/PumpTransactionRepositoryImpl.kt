package com.vn.bomnuocv1.data.repository

import com.vn.bomnuocv1.data.mapper.toDomain
import com.vn.bomnuocv1.data.remote.datasource.PumpTransactionRemoteDataSource
import com.vn.bomnuocv1.domain.model.PumpTransaction
import com.vn.bomnuocv1.domain.repository.CreatePumpTransactionParams
import com.vn.bomnuocv1.domain.repository.PumpTransactionRepository
import com.vn.bomnuocv1.domain.repository.UpdatePumpTransactionParams
import javax.inject.Inject

class PumpTransactionRepositoryImpl @Inject constructor(
    private val remoteDataSource: PumpTransactionRemoteDataSource
) : PumpTransactionRepository {

    override suspend fun getTransactions(
        farmerId: String?,
        keyword: String?
    ): Result<List<PumpTransaction>> {
        return try {
            val dtos = remoteDataSource.getTransactions(farmerId, keyword)
            Result.success(dtos.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createTransaction(
        params: CreatePumpTransactionParams
    ): Result<PumpTransaction> {
        return try {
            val dto = remoteDataSource.createTransaction(params)
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTransaction(
        id: String,
        params: UpdatePumpTransactionParams
    ): Result<PumpTransaction> {
        return try {
            val dto = remoteDataSource.updateTransaction(id, params)
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTransaction(id: String): Result<Unit> {
        return try {
            remoteDataSource.deleteTransaction(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
