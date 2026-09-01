package com.bomnuocv1.application.port.in;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class SavePricingRuleCommand {

    private final UUID ownerId;
    private final String pricingType;
    private final String unitLabel;
    private final BigDecimal unitPrice;
    private final LocalDate effectiveFrom;
}
