package com.vn.bomnuocv1.domain.model

data class Farmer(
    val id: String,
    val fullName: String,
    val phoneNumber: String?,
    val areaNote: String?,
    val createdAt: String? = null
)
