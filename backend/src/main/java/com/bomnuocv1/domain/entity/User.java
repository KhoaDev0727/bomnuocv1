package com.bomnuocv1.domain.entity;

import com.bomnuocv1.domain.valueobject.PhoneNumber;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class User {

    private final UUID id;
    private final PhoneNumber phoneNumber;
    private String pinHash;
    private String fullName;
    private final Role role;
    private boolean active;
    private final Instant createdAt;
    private Instant updatedAt;

    public User(UUID id, PhoneNumber phoneNumber, String pinHash, String fullName, Role role, boolean active, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.pinHash = pinHash;
        this.fullName = fullName;
        this.role = role;
        this.active = active;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
    }

    public static User createNewUser(PhoneNumber phoneNumber, String pinHash, String fullName, Role role) {
        Instant now = Instant.now();
        return User.builder()
                .id(UUID.randomUUID())
                .phoneNumber(phoneNumber)
                .pinHash(pinHash)
                .fullName(fullName != null ? fullName.trim() : "")
                .role(role)
                .active(true)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public void updateFullName(String newFullName) {
        if (newFullName != null && !newFullName.trim().isEmpty()) {
            this.fullName = newFullName.trim();
            this.updatedAt = Instant.now();
        }
    }

    public void updatePinHash(String newPinHash) {
        if (newPinHash != null && !newPinHash.trim().isEmpty()) {
            this.pinHash = newPinHash;
            this.updatedAt = Instant.now();
        }
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = Instant.now();
    }

    public void activate() {
        this.active = true;
        this.updatedAt = Instant.now();
    }
}
