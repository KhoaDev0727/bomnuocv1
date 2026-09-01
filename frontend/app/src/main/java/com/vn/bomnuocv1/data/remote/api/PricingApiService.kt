package com.vn.bomnuocv1.data.remote.api

import com.vn.bomnuocv1.data.remote.dto.ApiResponseDto
import com.vn.bomnuocv1.data.remote.dto.LandUnitOptionDto
import com.vn.bomnuocv1.data.remote.dto.PricingRuleResponseDto
import com.vn.bomnuocv1.data.remote.dto.SavePricingRuleRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PricingApiService {

    @GET("api/v1/pricing-rules/active")
    suspend fun getActivePricingRules(): Response<ApiResponseDto<List<PricingRuleResponseDto>>>

    @GET("api/v1/pricing-rules")
    suspend fun getAllPricingRules(): Response<ApiResponseDto<List<PricingRuleResponseDto>>>

    @POST("api/v1/pricing-rules")
    suspend fun savePricingRule(
        @Body request: SavePricingRuleRequestDto
    ): Response<ApiResponseDto<PricingRuleResponseDto>>

    @GET("api/v1/pricing-rules/units")
    suspend fun getLandUnitOptions(): Response<ApiResponseDto<List<LandUnitOptionDto>>>
}
