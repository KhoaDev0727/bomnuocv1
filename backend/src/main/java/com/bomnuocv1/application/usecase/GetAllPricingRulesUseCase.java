package com.bomnuocv1.application.usecase;

import com.bomnuocv1.application.dto.PricingRuleResult;

import java.util.List;
import java.util.UUID;

public interface GetAllPricingRulesUseCase {

    List<PricingRuleResult> execute(UUID ownerId);
}
