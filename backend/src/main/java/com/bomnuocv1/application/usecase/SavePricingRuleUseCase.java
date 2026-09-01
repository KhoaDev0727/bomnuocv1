package com.bomnuocv1.application.usecase;

import com.bomnuocv1.application.dto.PricingRuleResult;
import com.bomnuocv1.application.port.in.SavePricingRuleCommand;

public interface SavePricingRuleUseCase {

    PricingRuleResult execute(SavePricingRuleCommand command);
}
