package com.vn.bomnuocv1.domain.model

data class User(
    val id: String,
    val phoneNumber: String,
    val fullName: String,
    val roleCode: String,
    val roleName: String,
    val active: Boolean
)
