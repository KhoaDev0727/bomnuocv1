package com.bomnuocv1.presentation.mapper;

import com.bomnuocv1.application.dto.DashboardSummaryResult;
import com.bomnuocv1.application.dto.RecentTransactionResult;
import com.bomnuocv1.presentation.dto.response.DashboardSummaryResponse;
import com.bomnuocv1.presentation.dto.response.RecentTransactionResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DashboardApiMapper {

    public DashboardSummaryResponse toResponse(DashboardSummaryResult result) {
        if (result == null) {
            return null;
        }
        return DashboardSummaryResponse.builder()
                .totalUncollectedDebt(result.getTotalUncollectedDebt())
                .formattedTotalDebt(result.getFormattedTotalDebt())
                .todayPumpCount(result.getTodayPumpCount())
                .recentTransactions(result.getRecentTransactions().stream()
                        .map(this::toResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public RecentTransactionResponse toResponse(RecentTransactionResult result) {
        if (result == null) {
            return null;
        }
        return RecentTransactionResponse.builder()
                .id(result.getId())
                .type(result.getType())
                .farmerName(result.getFarmerName())
                .details(result.getDetails())
                .amount(result.getAmount())
                .formattedAmount(result.getFormattedAmount())
                .statusBadge(result.getStatusBadge())
                .createdAt(result.getCreatedAt())
                .build();
    }
}
