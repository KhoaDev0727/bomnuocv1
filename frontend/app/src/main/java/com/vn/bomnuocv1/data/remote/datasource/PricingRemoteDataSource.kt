package com.vn.bomnuocv1.data.remote.datasource

import com.vn.bomnuocv1.data.remote.api.PricingApiService
import com.vn.bomnuocv1.data.remote.dto.LandUnitOptionDto
import com.vn.bomnuocv1.data.remote.dto.PricingRuleResponseDto
import com.vn.bomnuocv1.data.remote.dto.SavePricingRuleRequestDto
import java.math.BigDecimal
import javax.inject.Inject

class PricingRemoteDataSource @Inject constructor(
    private val apiService: PricingApiService
) {
    suspend fun getActivePricingRules(): List<PricingRuleResponseDto> {
        val response = apiService.getActivePricingRules()
        if (response.isSuccessful && response.body()?.data != null) {
            return response.body()!!.data!!
        }
        throw Exception(response.body()?.message ?: "Không thể tải danh sách đơn giá.")
    }

    suspend fun getAllPricingRules(): List<PricingRuleResponseDto> {
        val response = apiService.getAllPricingRules()
        if (response.isSuccessful && response.body()?.data != null) {
            return response.body()!!.data!!
        }
        throw Exception(response.body()?.message ?: "Không thể tải lịch sử đơn giá.")
    }

    suspend fun savePricingRule(
        pricingType: String,
        unitLabel: String,
        unitPrice: BigDecimal,
        effectiveFrom: String?
    ): PricingRuleResponseDto {
        val request = SavePricingRuleRequestDto(
            pricingType = pricingType,
            unitLabel = unitLabel,
            unitPrice = unitPrice,
            effectiveFrom = effectiveFrom
        )
        val response = apiService.savePricingRule(request)
        if (response.isSuccessful && response.body()?.data != null) {
            return response.body()!!.data!!
        }
        throw Exception(response.body()?.message ?: "Không thể thiết lập đơn giá.")
    }

    suspend fun getLandUnitOptions(): List<LandUnitOptionDto> {
        val response = apiService.getLandUnitOptions()
        if (response.isSuccessful && response.body()?.data != null) {
            return response.body()!!.data!!
        }
        throw Exception(response.body()?.message ?: "Không thể tải danh mục quy chuẩn đơn vị.")
    }
}
