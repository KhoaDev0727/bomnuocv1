package com.bomnuocv1.domain.valueobject;

import lombok.Getter;

@Getter
public enum PricingType {
    PER_AREA("per_area", "Theo diện tích"),
    PER_HOUR("per_hour", "Theo thời gian (giờ)");

    private final String code;
    private final String description;

    PricingType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static PricingType fromCode(String code) {
        if (code == null) {
            return PER_AREA;
        }
        for (PricingType type : values()) {
            if (type.code.equalsIgnoreCase(code.trim()) || type.name().equalsIgnoreCase(code.trim())) {
                return type;
            }
        }
        return PER_AREA;
    }
}
