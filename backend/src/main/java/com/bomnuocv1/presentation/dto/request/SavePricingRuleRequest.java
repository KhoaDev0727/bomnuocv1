package com.bomnuocv1.presentation.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavePricingRuleRequest {

    @NotBlank(message = "Loại đơn giá không được để trống (per_area hoặc per_hour).")
    private String pricingType;

    @NotBlank(message = "Tên đơn vị không được để trống (ví dụ: công nhỏ (1.000m²), công lớn (1.296m²), giờ,...).")
    private String unitLabel;

    @NotNull(message = "Đơn giá không được để trống.")
    @DecimalMin(value = "0.0", inclusive = true, message = "Đơn giá không được nhỏ hơn 0.")
    private BigDecimal unitPrice;

    private LocalDate effectiveFrom;
}
