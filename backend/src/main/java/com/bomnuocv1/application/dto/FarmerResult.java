package com.bomnuocv1.application.dto;

import com.bomnuocv1.domain.entity.Farmer;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class FarmerResult {

    private final UUID id;
    private final UUID ownerId;
    private final String fullName;
    private final String phoneNumber;
    private final String areaNote;
    private final boolean deleted;
    private final UUID clientUuid;
    private final Instant createdAt;
    private final Instant updatedAt;

    public static FarmerResult fromDomain(Farmer farmer) {
        if (farmer == null) {
            return null;
        }
        return FarmerResult.builder()
                .id(farmer.getId())
                .ownerId(farmer.getOwnerId())
                .fullName(farmer.getFullName())
                .phoneNumber(farmer.getPhoneNumber())
                .areaNote(farmer.getAreaNote())
                .deleted(farmer.isDeleted())
                .clientUuid(farmer.getClientUuid())
                .createdAt(farmer.getCreatedAt())
                .updatedAt(farmer.getUpdatedAt())
                .build();
    }
}
