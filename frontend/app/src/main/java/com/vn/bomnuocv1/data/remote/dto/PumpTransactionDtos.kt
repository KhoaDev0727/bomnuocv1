package com.vn.bomnuocv1.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class RecordPumpTransactionRequestDto(
    @SerializedName("farmerId") val farmerId: String,
    @SerializedName("pricingRuleId") val pricingRuleId: String? = null,
    @SerializedName("transactionDate") val transactionDate: String,
    @SerializedName("quantity") val quantity: BigDecimal,
    @SerializedName("quantityUnit") val quantityUnit: String,
    @SerializedName("unitPrice") val unitPrice: BigDecimal,
    @SerializedName("initialPaidAmount") val initialPaidAmount: BigDecimal? = null,
    @SerializedName("note") val note: String? = null,
    @SerializedName("clientUuid") val clientUuid: String? = null
)

data class UpdatePumpTransactionRequestDto(
    @SerializedName("farmerId") val farmerId: String,
    @SerializedName("pricingRuleId") val pricingRuleId: String? = null,
    @SerializedName("transactionDate") val transactionDate: String,
    @SerializedName("quantity") val quantity: BigDecimal,
    @SerializedName("quantityUnit") val quantityUnit: String,
    @SerializedName("unitPrice") val unitPrice: BigDecimal,
    @SerializedName("note") val note: String? = null
)

data class PumpTransactionResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("farmerId") val farmerId: String,
    @SerializedName("farmerFullName") val farmerFullName: String?,
    @SerializedName("farmerPhone") val farmerPhone: String?,
    @SerializedName("pricingRuleId") val pricingRuleId: String?,
    @SerializedName("transactionDate") val transactionDate: String,
    @SerializedName("quantity") val quantity: BigDecimal,
    @SerializedName("quantityUnit") val quantityUnit: String,
    @SerializedName("unitPrice") val unitPrice: BigDecimal,
    @SerializedName("amountDue") val amountDue: BigDecimal,
    @SerializedName("paidAmount") val paidAmount: BigDecimal?,
    @SerializedName("remainingDebt") val remainingDebt: BigDecimal?,
    @SerializedName("paymentStatus") val paymentStatus: String?,
    @SerializedName("note") val note: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("updatedAt") val updatedAt: String?
)
