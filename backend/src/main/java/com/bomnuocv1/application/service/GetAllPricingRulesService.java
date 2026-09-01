package com.bomnuocv1.application.service;

import com.bomnuocv1.application.dto.PricingRuleResult;
import com.bomnuocv1.application.usecase.GetAllPricingRulesUseCase;
import com.bomnuocv1.domain.entity.PricingRule;
import com.bomnuocv1.domain.repository.PricingRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetAllPricingRulesService implements GetAllPricingRulesUseCase {

    private final PricingRuleRepository pricingRuleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PricingRuleResult> execute(UUID ownerId) {
        List<PricingRule> rules = pricingRuleRepository.findByOwnerId(ownerId);
        return rules.stream()
                .map(this::toResult)
                .collect(Collectors.toList());
    }

    private PricingRuleResult toResult(PricingRule rule) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMAN);
        DecimalFormat formatter = new DecimalFormat("#,###", symbols);
        String formattedPrice = formatter.format(rule.getUnitPrice()) + " đ";

        return PricingRuleResult.builder()
                .id(rule.getId())
                .ownerId(rule.getOwnerId())
                .pricingType(rule.getPricingType().getCode())
                .pricingTypeDescription(rule.getPricingType().getDescription())
                .unitLabel(rule.getUnitLabel())
                .unitPrice(rule.getUnitPrice())
                .formattedUnitPrice(formattedPrice)
                .effectiveFrom(rule.getEffectiveFrom())
                .effectiveTo(rule.getEffectiveTo())
                .active(rule.isActive())
                .createdAt(rule.getCreatedAt())
                .build();
    }
}
