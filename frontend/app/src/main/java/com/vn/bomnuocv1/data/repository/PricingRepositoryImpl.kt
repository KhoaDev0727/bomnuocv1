package com.vn.bomnuocv1.data.repository

import com.vn.bomnuocv1.data.mapper.toDomain
import com.vn.bomnuocv1.data.remote.datasource.PricingRemoteDataSource
import com.vn.bomnuocv1.domain.model.LandUnitOption
import com.vn.bomnuocv1.domain.model.PricingRule
import com.vn.bomnuocv1.domain.repository.PricingRepository
import java.math.BigDecimal
import javax.inject.Inject

class PricingRepositoryImpl @Inject constructor(
    private val remoteDataSource: PricingRemoteDataSource
) : PricingRepository {

    override suspend fun getActivePricingRules(): Result<List<PricingRule>> {
        return try {
            val dtos = remoteDataSource.getActivePricingRules()
            Result.success(dtos.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllPricingRules(): Result<List<PricingRule>> {
        return try {
            val dtos = remoteDataSource.getAllPricingRules()
            Result.success(dtos.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun savePricingRule(
        pricingType: String,
        unitLabel: String,
        unitPrice: BigDecimal,
        effectiveFrom: String?
    ): Result<PricingRule> {
        return try {
            val dto = remoteDataSource.savePricingRule(pricingType, unitLabel, unitPrice, effectiveFrom)
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getLandUnitOptions(): Result<List<LandUnitOption>> {
        return try {
            val dtos = remoteDataSource.getLandUnitOptions()
            Result.success(dtos.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
