package com.bomnuocv1.application.port.in;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CreateFarmerCommand {

    private final UUID ownerId;
    private final String fullName;
    private final String phoneNumber;
    private final String areaNote;
    private final UUID clientUuid;
}
