package com.bomnuocv1.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private UUID id;
    private UUID farmerId;
    private String farmerFullName;
    private UUID transactionId;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String note;
    private UUID clientUuid;
    private Instant createdAt;
}
