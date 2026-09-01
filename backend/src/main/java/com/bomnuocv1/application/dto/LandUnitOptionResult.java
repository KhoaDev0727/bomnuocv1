package com.bomnuocv1.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class LandUnitOptionResult {

    private final String code;
    private final String label;
    private final String displayName;
    private final BigDecimal squareMeters;
    private final BigDecimal defaultPrice;
    private final String formattedDefaultPrice;
    private final String description;
}
