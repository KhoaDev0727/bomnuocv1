package com.bomnuocv1.application.service;

import com.bomnuocv1.application.dto.PricingRuleResult;
import com.bomnuocv1.application.port.in.SavePricingRuleCommand;
import com.bomnuocv1.application.usecase.SavePricingRuleUseCase;
import com.bomnuocv1.domain.entity.PricingRule;
import com.bomnuocv1.domain.exception.InvalidPricingException;
import com.bomnuocv1.domain.repository.PricingRuleRepository;
import com.bomnuocv1.domain.valueobject.PricingType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SavePricingRuleService implements SavePricingRuleUseCase {

    private final PricingRuleRepository pricingRuleRepository;

    @Override
    @Transactional
    public PricingRuleResult execute(SavePricingRuleCommand command) {
        if (command.getUnitPrice() == null || command.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPricingException("Đơn giá bơm nước không hợp lệ hoặc nhỏ hơn 0.");
        }

        PricingType pricingType = PricingType.fromCode(command.getPricingType());
        String unitLabel = command.getUnitLabel() != null && !command.getUnitLabel().trim().isEmpty()
                ? command.getUnitLabel().trim()
                : (pricingType == PricingType.PER_HOUR ? "giờ" : "công nhỏ (1.000m²)");

        LocalDate effectiveFrom = command.getEffectiveFrom() != null ? command.getEffectiveFrom() : LocalDate.now();

        // Check if there is an existing active rule for this owner and unit
        Optional<PricingRule> activeRuleOpt = pricingRuleRepository.findActiveByOwnerIdAndUnitLabel(command.getOwnerId(), unitLabel);
        if (activeRuleOpt.isPresent()) {
            PricingRule oldRule = activeRuleOpt.get();
            // Terminate previous rule
            LocalDate terminationDate = effectiveFrom.minusDays(1);
            if (terminationDate.isBefore(oldRule.getEffectiveFrom())) {
                terminationDate = oldRule.getEffectiveFrom();
            }
            oldRule.terminate(terminationDate);
            pricingRuleRepository.save(oldRule);
            log.info("Terminated old pricing rule: {} with end date: {}", oldRule.getId(), terminationDate);
        }

        // Create new active rule
        PricingRule newRule = PricingRule.createNew(
                command.getOwnerId(),
                pricingType,
                unitLabel,
                command.getUnitPrice(),
                effectiveFrom
        );

        PricingRule saved = pricingRuleRepository.save(newRule);
        log.info("Created new pricing rule: {} for owner: {} with price: {}", saved.getId(), command.getOwnerId(), saved.getUnitPrice());

        return toResult(saved);
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
