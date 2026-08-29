package com.bomnuocv1.infrastructure.persistence.mapper;

import com.bomnuocv1.domain.entity.User;
import com.bomnuocv1.domain.valueobject.PhoneNumber;
import com.bomnuocv1.infrastructure.persistence.entity.UserJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPersistenceMapper {

    private final RolePersistenceMapper roleMapper;

    public User toDomain(UserJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return User.builder()
                .id(entity.getId())
                .phoneNumber(new PhoneNumber(entity.getPhoneNumber()))
                .pinHash(entity.getPinHash())
                .fullName(entity.getFullName())
                .role(roleMapper.toDomain(entity.getRole()))
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public UserJpaEntity toEntity(User domain) {
        if (domain == null) {
            return null;
        }
        return UserJpaEntity.builder()
                .id(domain.getId())
                .phoneNumber(domain.getPhoneNumber().getValue())
                .pinHash(domain.getPinHash())
                .fullName(domain.getFullName())
                .role(roleMapper.toEntity(domain.getRole()))
                .active(domain.isActive())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
