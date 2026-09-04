package com.vn.bomnuocv1.data.mapper

import com.vn.bomnuocv1.data.remote.dto.FarmerResponseDto
import com.vn.bomnuocv1.domain.model.Farmer

fun FarmerResponseDto.toDomain(): Farmer {
    return Farmer(
        id = id,
        fullName = fullName,
        phoneNumber = phoneNumber,
        areaNote = areaNote,
        createdAt = createdAt
    )
}
