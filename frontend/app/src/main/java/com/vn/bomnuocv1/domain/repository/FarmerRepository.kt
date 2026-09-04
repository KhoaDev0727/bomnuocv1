package com.vn.bomnuocv1.domain.repository

import com.vn.bomnuocv1.domain.model.Farmer

interface FarmerRepository {

    suspend fun getFarmers(keyword: String? = null): Result<List<Farmer>>

    suspend fun createFarmer(fullName: String, phoneNumber: String?, areaNote: String?): Result<Farmer>

    suspend fun updateFarmer(id: String, fullName: String, phoneNumber: String?, areaNote: String?): Result<Farmer>

    suspend fun deleteFarmer(id: String): Result<Unit>
}
