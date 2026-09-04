package com.bomnuocv1.presentation.mapper;

import com.bomnuocv1.application.dto.FarmerResult;
import com.bomnuocv1.application.port.in.CreateFarmerCommand;
import com.bomnuocv1.application.port.in.UpdateFarmerCommand;
import com.bomnuocv1.presentation.dto.request.CreateFarmerRequest;
import com.bomnuocv1.presentation.dto.request.UpdateFarmerRequest;
import com.bomnuocv1.presentation.dto.response.FarmerResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FarmerApiMapper {

    public CreateFarmerCommand toCommand(CreateFarmerRequest request, UUID ownerId) {
        if (request == null) {
            return null;
        }
        return CreateFarmerCommand.builder()
                .ownerId(ownerId)
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .areaNote(request.getAreaNote())
                .clientUuid(request.getClientUuid())
                .build();
    }

    public UpdateFarmerCommand toCommand(UUID id, UpdateFarmerRequest request, UUID ownerId) {
        if (request == null) {
            return null;
        }
        return UpdateFarmerCommand.builder()
                .id(id)
                .ownerId(ownerId)
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .areaNote(request.getAreaNote())
                .build();
    }

    public FarmerResponse toResponse(FarmerResult result) {
        if (result == null) {
            return null;
        }
        return FarmerResponse.builder()
                .id(result.getId())
                .fullName(result.getFullName())
                .phoneNumber(result.getPhoneNumber())
                .areaNote(result.getAreaNote())
                .createdAt(result.getCreatedAt())
                .updatedAt(result.getUpdatedAt())
                .build();
    }
}
