package com.bomnuocv1.application.port.in;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class RecordPaymentCommand {

    private final UUID ownerId;
    private final UUID farmerId;
    private final UUID transactionId;
    private final BigDecimal amount;
    private final LocalDate paymentDate;
    private final String note;
    private final UUID clientUuid;
}
