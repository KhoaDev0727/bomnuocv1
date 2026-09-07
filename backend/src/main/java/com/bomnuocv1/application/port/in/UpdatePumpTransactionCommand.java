package com.bomnuocv1.application.port.in;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class UpdatePumpTransactionCommand {

    private final UUID id;
    private final UUID ownerId;
    private final UUID farmerId;
    private final UUID pricingRuleId;
    private final LocalDate transactionDate;
    private final BigDecimal quantity;
    private final String quantityUnit;
    private final BigDecimal unitPrice;
    private final String note;
}
