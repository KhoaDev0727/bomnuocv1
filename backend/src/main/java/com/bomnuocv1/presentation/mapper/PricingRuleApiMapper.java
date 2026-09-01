package com.bomnuocv1.presentation.mapper;

import com.bomnuocv1.application.dto.LandUnitOptionResult;
import com.bomnuocv1.application.dto.PricingRuleResult;
import com.bomnuocv1.application.port.in.SavePricingRuleCommand;
import com.bomnuocv1.presentation.dto.request.SavePricingRuleRequest;
import com.bomnuocv1.presentation.dto.response.LandUnitOptionResponse;
import com.bomnuocv1.presentation.dto.response.PricingRuleResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PricingRuleApiMapper {

    public SavePricingRuleCommand toCommand(SavePricingRuleRequest request, UUID ownerId) {
        if (request == null) {
            return null;
        }
        return SavePricingRuleCommand.builder()
                .ownerId(ownerId)
                .pricingType(request.getPricingType())
                .unitLabel(request.getUnitLabel())
                .unitPrice(request.getUnitPrice())
                .effectiveFrom(request.getEffectiveFrom())
                .build();
    }

    public PricingRuleResponse toResponse(PricingRuleResult result) {
        if (result == null) {
            return null;
        }
        return PricingRuleResponse.builder()
                .id(result.getId())
                .ownerId(result.getOwnerId())
                .pricingType(result.getPricingType())
                .pricingTypeDescription(result.getPricingTypeDescription())
                .unitLabel(result.getUnitLabel())
                .unitPrice(result.getUnitPrice())
                .formattedUnitPrice(result.getFormattedUnitPrice())
                .effectiveFrom(result.getEffectiveFrom())
                .effectiveTo(result.getEffectiveTo())
                .active(result.isActive())
                .createdAt(result.getCreatedAt())
                .build();
    }

    public LandUnitOptionResponse toResponse(LandUnitOptionResult result) {
        if (result == null) {
            return null;
        }
        return LandUnitOptionResponse.builder()
                .code(result.getCode())
                .label(result.getLabel())
                .displayName(result.getDisplayName())
                .squareMeters(result.getSquareMeters())
                .defaultPrice(result.getDefaultPrice())
                .formattedDefaultPrice(result.getFormattedDefaultPrice())
                .description(result.getDescription())
                .build();
    }
}
