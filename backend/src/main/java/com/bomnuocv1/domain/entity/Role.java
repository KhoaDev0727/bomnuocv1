package com.bomnuocv1.domain.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class Role {

    private final UUID id;
    private final String code;
    private final String name;
    private final String description;
    private final Instant createdAt;
    private final Instant updatedAt;

    public Role(UUID id, String code, String name, String description, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
    }

    public static Role createOwnerRole() {
        return Role.builder()
                .id(UUID.randomUUID())
                .code("owner")
                .name("Chủ trạm")
                .description("Tài khoản chủ trạm bơm có đầy đủ quyền quản lý")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    public static Role createStaffRole() {
        return Role.builder()
                .id(UUID.randomUUID())
                .code("staff")
                .name("Nhân viên")
                .description("Tài khoản nhân viên phụ hỗ trợ vận hành")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    public boolean isOwner() {
        return "owner".equalsIgnoreCase(this.code);
    }
}
