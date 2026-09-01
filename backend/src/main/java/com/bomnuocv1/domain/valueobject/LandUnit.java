package com.bomnuocv1.domain.valueobject;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum LandUnit {
    CONG_NHO_1000("công nhỏ (1.000m²)", "Công tầm nhỏ", new BigDecimal("1000"), new BigDecimal("90000"), "Chuẩn Nam Bộ / 1.000 mét vuông"),
    CONG_LON_1296("công lớn (1.296m²)", "Công tầm lớn (3m)", new BigDecimal("1296"), new BigDecimal("115000"), "Tây Nam Bộ / 144 tầm vuông (tầm 3m) = 1.296 m²"),
    CONG_LON_1440("công lớn (1.440m²)", "Công tầm lớn (3m25)", new BigDecimal("1440"), new BigDecimal("130000"), "Tây Nam Bộ / tầm cắt 3m25 = ~1.440 m²"),
    SAO_BAC_BO_360("sào Bắc Bộ (360m²)", "Sào Bắc Bộ", new BigDecimal("360"), new BigDecimal("35000"), "Bắc Bộ / 360 mét vuông"),
    SAO_TRUNG_BO_500("sào Trung Bộ (500m²)", "Sào Trung Bộ", new BigDecimal("500"), new BigDecimal("45000"), "Trung Bộ / 500 mét vuông"),
    HECTA_10000("hecta (10.000m²)", "Hecta (ha)", new BigDecimal("10000"), new BigDecimal("900000"), "1 Hecta = 10.000 mét vuông (10 công nhỏ)"),
    HOUR("giờ", "Theo giờ bơm", BigDecimal.ZERO, new BigDecimal("60000"), "Tính tiền theo thời gian bơm nước");

    private final String label;
    private final String displayName;
    private final BigDecimal squareMeters;
    private final BigDecimal defaultPrice;
    private final String description;

    LandUnit(String label, String displayName, BigDecimal squareMeters, BigDecimal defaultPrice, String description) {
        this.label = label;
        this.displayName = displayName;
        this.squareMeters = squareMeters;
        this.defaultPrice = defaultPrice;
        this.description = description;
    }

    public static LandUnit fromLabel(String label) {
        if (label == null) {
            return CONG_NHO_1000;
        }
        for (LandUnit unit : values()) {
            if (unit.label.equalsIgnoreCase(label.trim()) || unit.name().equalsIgnoreCase(label.trim())) {
                return unit;
            }
        }
        return CONG_NHO_1000;
    }
}
