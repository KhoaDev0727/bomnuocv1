package com.bomnuocv1.domain.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class Farmer {

    private final UUID id;
    private final UUID ownerId;
    private String fullName;
    private String phoneNumber;
    private String areaNote;
    private boolean deleted;
    private UUID clientUuid;
    private final Instant createdAt;
    private Instant updatedAt;

    public Farmer(UUID id, UUID ownerId, String fullName, String phoneNumber, String areaNote, boolean deleted, UUID clientUuid, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.ownerId = ownerId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.areaNote = areaNote;
        this.deleted = deleted;
        this.clientUuid = clientUuid;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
    }

    public static Farmer createNew(UUID ownerId, String fullName, String phoneNumber, String areaNote, UUID clientUuid) {
        Instant now = Instant.now();
        String trimmedPhone = (phoneNumber != null && !phoneNumber.trim().isEmpty()) ? phoneNumber.trim() : null;
        String trimmedArea = (areaNote != null && !areaNote.trim().isEmpty()) ? areaNote.trim() : null;
        return Farmer.builder()
                .id(UUID.randomUUID())
                .ownerId(ownerId)
                .fullName(fullName != null ? fullName.trim() : "")
                .phoneNumber(trimmedPhone)
                .areaNote(trimmedArea)
                .deleted(false)
                .clientUuid(clientUuid)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public void updateInfo(String fullName, String phoneNumber, String areaNote) {
        if (fullName != null && !fullName.trim().isEmpty()) {
            this.fullName = fullName.trim();
        }
        this.phoneNumber = (phoneNumber != null && !phoneNumber.trim().isEmpty()) ? phoneNumber.trim() : null;
        this.areaNote = (areaNote != null && !areaNote.trim().isEmpty()) ? areaNote.trim() : null;
        this.updatedAt = Instant.now();
    }

    public void markDeleted() {
        this.deleted = true;
        this.updatedAt = Instant.now();
    }
}
