package com.bomnuocv1.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LandUnitOptionResponse {

    private String code;
    private String label;
    private String displayName;
    private BigDecimal squareMeters;
    private BigDecimal defaultPrice;
    private String formattedDefaultPrice;
    private String description;
}
