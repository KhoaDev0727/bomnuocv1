package com.bomnuocv1.presentation.controller;

import com.bomnuocv1.application.dto.LandUnitOptionResult;
import com.bomnuocv1.application.dto.PricingRuleResult;
import com.bomnuocv1.application.port.in.SavePricingRuleCommand;
import com.bomnuocv1.application.usecase.GetActivePricingRulesUseCase;
import com.bomnuocv1.application.usecase.GetAllPricingRulesUseCase;
import com.bomnuocv1.application.usecase.GetLandUnitOptionsUseCase;
import com.bomnuocv1.application.usecase.SavePricingRuleUseCase;
import com.bomnuocv1.presentation.dto.request.SavePricingRuleRequest;
import com.bomnuocv1.presentation.dto.response.ApiResponse;
import com.bomnuocv1.presentation.dto.response.LandUnitOptionResponse;
import com.bomnuocv1.presentation.dto.response.PricingRuleResponse;
import com.bomnuocv1.presentation.mapper.PricingRuleApiMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pricing-rules")
@RequiredArgsConstructor
public class PricingRuleController {

    private final SavePricingRuleUseCase savePricingRuleUseCase;
    private final GetActivePricingRulesUseCase getActivePricingRulesUseCase;
    private final GetAllPricingRulesUseCase getAllPricingRulesUseCase;
    private final GetLandUnitOptionsUseCase getLandUnitOptionsUseCase;
    private final PricingRuleApiMapper mapper;

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<PricingRuleResponse>>> getActivePricingRules() {
        UUID currentUserId = getCurrentUserId();
        List<PricingRuleResult> results = getActivePricingRulesUseCase.execute(currentUserId);
        List<PricingRuleResponse> response = results.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách đơn giá đang áp dụng thành công!", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PricingRuleResponse>>> getAllPricingRules() {
        UUID currentUserId = getCurrentUserId();
        List<PricingRuleResult> results = getAllPricingRulesUseCase.execute(currentUserId);
        List<PricingRuleResponse> response = results.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Lấy lịch sử đơn giá thành công!", response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PricingRuleResponse>> savePricingRule(@Valid @RequestBody SavePricingRuleRequest request) {
        UUID currentUserId = getCurrentUserId();
        SavePricingRuleCommand command = mapper.toCommand(request, currentUserId);
        PricingRuleResult result = savePricingRuleUseCase.execute(command);
        PricingRuleResponse response = mapper.toResponse(result);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Thiết lập đơn giá bơm nước thành công!", response));
    }

    @GetMapping("/units")
    public ResponseEntity<ApiResponse<List<LandUnitOptionResponse>>> getLandUnitOptions() {
        List<LandUnitOptionResult> results = getLandUnitOptionsUseCase.execute();
        List<LandUnitOptionResponse> response = results.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách quy chuẩn đơn vị thành công!", response));
    }

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UUID) authentication.getPrincipal();
    }
}
