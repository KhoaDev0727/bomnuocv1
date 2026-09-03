package com.bomnuocv1.application.service;

import com.bomnuocv1.application.usecase.DeletePricingRuleUseCase;
import com.bomnuocv1.domain.entity.PricingRule;
import com.bomnuocv1.domain.exception.PricingRuleNotFoundException;
import com.bomnuocv1.domain.repository.PricingRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeletePricingRuleService implements DeletePricingRuleUseCase {

    private final PricingRuleRepository pricingRuleRepository;

    @Override
    @Transactional
    public void execute(UUID pricingRuleId, UUID ownerId) {
        log.info("Attempting to delete pricing rule: {} for owner: {}", pricingRuleId, ownerId);

        PricingRule rule = pricingRuleRepository.findById(pricingRuleId)
                .orElseThrow(() -> new PricingRuleNotFoundException("Không tìm thấy đơn giá bơm cần xóa."));

        if (!rule.getOwnerId().equals(ownerId)) {
            log.warn("Unauthorized attempt to delete pricing rule: {} by user: {}", pricingRuleId, ownerId);
            throw new PricingRuleNotFoundException("Không tìm thấy đơn giá bơm hoặc bạn không có quyền xóa.");
        }

        pricingRuleRepository.delete(rule);
        log.info("Successfully deleted pricing rule: {} for owner: {}", pricingRuleId, ownerId);
    }
}
