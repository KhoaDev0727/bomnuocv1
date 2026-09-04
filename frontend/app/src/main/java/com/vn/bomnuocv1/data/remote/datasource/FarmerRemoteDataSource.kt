package com.vn.bomnuocv1.data.remote.datasource

import com.google.gson.Gson
import com.vn.bomnuocv1.data.remote.api.FarmerApiService
import com.vn.bomnuocv1.data.remote.dto.ApiResponseDto
import com.vn.bomnuocv1.data.remote.dto.CreateFarmerRequestDto
import com.vn.bomnuocv1.data.remote.dto.FarmerResponseDto
import com.vn.bomnuocv1.data.remote.dto.UpdateFarmerRequestDto
import javax.inject.Inject

class FarmerRemoteDataSource @Inject constructor(
    private val apiService: FarmerApiService,
    private val gson: Gson
) {
    suspend fun getFarmers(keyword: String?): List<FarmerResponseDto> {
        val response = apiService.getFarmers(keyword)
        if (response.isSuccessful && response.body()?.data != null) {
            return response.body()!!.data!!
        }
        val errorMsg = parseErrorMessage(response.errorBody()?.string())
            ?: response.body()?.message
            ?: "Không thể tải danh sách nông dân."
        throw Exception(errorMsg)
    }

    suspend fun createFarmer(
        fullName: String,
        phoneNumber: String?,
        areaNote: String?
    ): FarmerResponseDto {
        val request = CreateFarmerRequestDto(
            fullName = fullName,
            phoneNumber = phoneNumber,
            areaNote = areaNote
        )
        val response = apiService.createFarmer(request)
        if (response.isSuccessful && response.body()?.data != null) {
            return response.body()!!.data!!
        }
        val errorMsg = parseErrorMessage(response.errorBody()?.string())
            ?: response.body()?.message
            ?: "Không thể thêm nông dân mới."
        throw Exception(errorMsg)
    }

    suspend fun updateFarmer(
        id: String,
        fullName: String,
        phoneNumber: String?,
        areaNote: String?
    ): FarmerResponseDto {
        val request = UpdateFarmerRequestDto(
            fullName = fullName,
            phoneNumber = phoneNumber,
            areaNote = areaNote
        )
        val response = apiService.updateFarmer(id, request)
        if (response.isSuccessful && response.body()?.data != null) {
            return response.body()!!.data!!
        }
        val errorMsg = parseErrorMessage(response.errorBody()?.string())
            ?: response.body()?.message
            ?: "Không thể cập nhật hồ sơ nông dân."
        throw Exception(errorMsg)
    }

    suspend fun deleteFarmer(id: String) {
        val response = apiService.deleteFarmer(id)
        if (!response.isSuccessful) {
            val errorMsg = parseErrorMessage(response.errorBody()?.string())
                ?: response.body()?.message
                ?: "Không thể xóa hồ sơ nông dân."
            throw Exception(errorMsg)
        }
    }

    private fun parseErrorMessage(errorJson: String?): String? {
        if (errorJson.isNullOrBlank()) return null
        return try {
            val parsed = gson.fromJson(errorJson, ApiResponseDto::class.java)
            parsed.message
        } catch (_: Exception) {
            null
        }
    }
}
