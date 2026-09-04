package com.vn.bomnuocv1.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreateFarmerRequestDto(
    @SerializedName("fullName") val fullName: String,
    @SerializedName("phoneNumber") val phoneNumber: String? = null,
    @SerializedName("areaNote") val areaNote: String? = null,
    @SerializedName("clientUuid") val clientUuid: String? = null
)

data class UpdateFarmerRequestDto(
    @SerializedName("fullName") val fullName: String,
    @SerializedName("phoneNumber") val phoneNumber: String? = null,
    @SerializedName("areaNote") val areaNote: String? = null
)

data class FarmerResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("phoneNumber") val phoneNumber: String?,
    @SerializedName("areaNote") val areaNote: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("updatedAt") val updatedAt: String?
)
