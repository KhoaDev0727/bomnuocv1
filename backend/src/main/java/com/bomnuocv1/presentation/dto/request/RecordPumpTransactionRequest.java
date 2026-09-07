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
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordPumpTransactionRequest {

    @NotNull(message = "Nông dân không được để trống.")
    private UUID farmerId;

    private UUID pricingRuleId;

    @NotNull(message = "Ngày bơm không được để trống.")
    private LocalDate transactionDate;

    @NotNull(message = "Số lượng (diện tích/giờ) không được để trống.")
    @DecimalMin(value = "0.01", message = "Số lượng phải lớn hơn 0.")
    private BigDecimal quantity;

    @NotBlank(message = "Đơn vị tính không được để trống.")
    private String quantityUnit;

    @NotNull(message = "Đơn giá không được để trống.")
    @DecimalMin(value = "0.00", message = "Đơn giá không được âm.")
    private BigDecimal unitPrice;

    @DecimalMin(value = "0.00", message = "Số tiền trả trước không được âm.")
    private BigDecimal initialPaidAmount;

    private String note;

    private UUID clientUuid;
}
