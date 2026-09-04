package com.bomnuocv1.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FarmerResponse {

    private UUID id;
    private String fullName;
    private String phoneNumber;
    private String areaNote;
    private Instant createdAt;
    private Instant updatedAt;
}
