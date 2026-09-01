package com.bomnuocv1.application.service;

import com.bomnuocv1.application.dto.PricingRuleResult;
import com.bomnuocv1.application.usecase.GetActivePricingRulesUseCase;
import com.bomnuocv1.domain.entity.PricingRule;
import com.bomnuocv1.domain.repository.PricingRuleRepository;
import com.bomnuocv1.domain.valueobject.LandUnit;
import com.bomnuocv1.domain.valueobject.PricingType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetActivePricingRulesService implements GetActivePricingRulesUseCase {

    private final PricingRuleRepository pricingRuleRepository;

    @Override
    @Transactional
    public List<PricingRuleResult> execute(UUID ownerId) {
        List<PricingRule> activeRules = pricingRuleRepository.findActiveByOwnerId(ownerId);

        // If station owner has no pricing rules configured yet, initialize standard defaults
        if (activeRules.isEmpty()) {
            activeRules = seedDefaultPricingRules(ownerId);
        }

        return activeRules.stream()
                .map(this::toResult)
                .collect(Collectors.toList());
    }

    private List<PricingRule> seedDefaultPricingRules(UUID ownerId) {
        List<PricingRule> defaults = new ArrayList<>();

        // 1. Công tầm nhỏ (1.000 m2) - Giá chuẩn 90.000 đ
        PricingRule standardCong = PricingRule.createNew(
                ownerId,
                PricingType.PER_AREA,
                LandUnit.CONG_NHO_1000.getLabel(),
                LandUnit.CONG_NHO_1000.getDefaultPrice(),
                LocalDate.now()
        );
        defaults.add(pricingRuleRepository.save(standardCong));

        // 2. Công tầm lớn (1.296 m2) - Giá 115.000 đ
        PricingRule largeCong = PricingRule.createNew(
                ownerId,
                PricingType.PER_AREA,
                LandUnit.CONG_LON_1296.getLabel(),
                LandUnit.CONG_LON_1296.getDefaultPrice(),
                LocalDate.now()
        );
        defaults.add(pricingRuleRepository.save(largeCong));

        // 3. Giờ bơm - Giá 60.000 đ/giờ
        PricingRule hourRule = PricingRule.createNew(
                ownerId,
                PricingType.PER_HOUR,
                LandUnit.HOUR.getLabel(),
                LandUnit.HOUR.getDefaultPrice(),
                LocalDate.now()
        );
        defaults.add(pricingRuleRepository.save(hourRule));

        log.info("Initialized default active pricing rules for owner: {}", ownerId);
        return defaults;
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
