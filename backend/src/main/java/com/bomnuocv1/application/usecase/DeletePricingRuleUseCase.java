package com.bomnuocv1.application.usecase;

import java.util.UUID;

public interface DeletePricingRuleUseCase {

    void execute(UUID pricingRuleId, UUID ownerId);
}
