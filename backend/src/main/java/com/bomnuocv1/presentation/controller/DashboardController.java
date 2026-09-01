package com.bomnuocv1.presentation.controller;

import com.bomnuocv1.application.dto.DashboardSummaryResult;
import com.bomnuocv1.application.usecase.GetDashboardSummaryUseCase;
import com.bomnuocv1.presentation.dto.response.ApiResponse;
import com.bomnuocv1.presentation.dto.response.DashboardSummaryResponse;
import com.bomnuocv1.presentation.mapper.DashboardApiMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final GetDashboardSummaryUseCase getDashboardSummaryUseCase;
    private final DashboardApiMapper mapper;

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<DashboardSummaryResponse>> getSummary() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID currentUserId = (UUID) authentication.getPrincipal();
        DashboardSummaryResult result = getDashboardSummaryUseCase.execute(currentUserId);
        DashboardSummaryResponse response = mapper.toResponse(result);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin tổng quan thành công!", response));
    }
}
