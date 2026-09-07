package com.bomnuocv1.presentation.dto.request;

import jakarta.validation.constraints.DecimalMin;
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
public class RecordPaymentRequest {

    @NotNull(message = "Nông dân không được để trống.")
    private UUID farmerId;

    private UUID transactionId;

    @NotNull(message = "Số tiền không được để trống.")
    @DecimalMin(value = "0.01", message = "Số tiền thanh toán phải lớn hơn 0.")
    private BigDecimal amount;

    @NotNull(message = "Ngày thanh toán không được để trống.")
    private LocalDate paymentDate;

    private String note;

    private UUID clientUuid;
}
