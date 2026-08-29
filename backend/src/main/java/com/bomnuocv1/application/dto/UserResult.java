package com.bomnuocv1.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class UserResult {
    private final UUID id;
    private final String phoneNumber;
    private final String fullName;
    private final String roleCode;
    private final String roleName;
    private final boolean active;
    private final Instant createdAt;
}
