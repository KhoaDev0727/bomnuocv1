package com.vn.bomnuocv1.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class RecordPaymentRequestDto(
    @SerializedName("farmerId") val farmerId: String,
    @SerializedName("transactionId") val transactionId: String? = null,
    @SerializedName("amount") val amount: BigDecimal,
    @SerializedName("paymentDate") val paymentDate: String,
    @SerializedName("note") val note: String? = null,
    @SerializedName("clientUuid") val clientUuid: String? = null
)

data class PaymentResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("farmerId") val farmerId: String,
    @SerializedName("farmerFullName") val farmerFullName: String?,
    @SerializedName("transactionId") val transactionId: String?,
    @SerializedName("amount") val amount: BigDecimal,
    @SerializedName("paymentDate") val paymentDate: String,
    @SerializedName("note") val note: String?,
    @SerializedName("createdAt") val createdAt: String?
)
