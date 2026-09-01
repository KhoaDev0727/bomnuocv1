package com.bomnuocv1.infrastructure.persistence.mapper;

import com.bomnuocv1.domain.entity.Farmer;
import com.bomnuocv1.infrastructure.persistence.entity.FarmerJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class FarmerPersistenceMapper {

    public Farmer toDomain(FarmerJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return Farmer.builder()
                .id(entity.getId())
                .ownerId(entity.getOwnerId())
                .fullName(entity.getFullName())
                .phoneNumber(entity.getPhoneNumber())
                .areaNote(entity.getAreaNote())
                .deleted(entity.isDeleted())
                .clientUuid(entity.getClientUuid())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public FarmerJpaEntity toEntity(Farmer domain) {
        if (domain == null) {
            return null;
        }
        return FarmerJpaEntity.builder()
                .id(domain.getId())
                .ownerId(domain.getOwnerId())
                .fullName(domain.getFullName())
                .phoneNumber(domain.getPhoneNumber())
                .areaNote(domain.getAreaNote())
                .deleted(domain.isDeleted())
                .clientUuid(domain.getClientUuid())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
