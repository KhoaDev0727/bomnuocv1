package com.vn.bomnuocv1.data.repository

import com.vn.bomnuocv1.data.mapper.toDomain
import com.vn.bomnuocv1.data.remote.datasource.PaymentRemoteDataSource
import com.vn.bomnuocv1.domain.model.Payment
import com.vn.bomnuocv1.domain.repository.CreatePaymentParams
import com.vn.bomnuocv1.domain.repository.PaymentRepository
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val remoteDataSource: PaymentRemoteDataSource
) : PaymentRepository {

    override suspend fun recordPayment(
        params: CreatePaymentParams
    ): Result<Payment> {
        return try {
            val dto = remoteDataSource.recordPayment(params)
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPayments(
        farmerId: String?,
        transactionId: String?
    ): Result<List<Payment>> {
        return try {
            val dtos = remoteDataSource.getPayments(farmerId, transactionId)
            Result.success(dtos.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
