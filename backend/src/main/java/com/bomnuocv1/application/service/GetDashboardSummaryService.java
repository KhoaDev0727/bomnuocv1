package com.bomnuocv1.application.service;

import com.bomnuocv1.application.dto.DashboardSummaryResult;
import com.bomnuocv1.application.dto.RecentTransactionResult;
import com.bomnuocv1.application.usecase.GetDashboardSummaryUseCase;
import com.bomnuocv1.domain.entity.DashboardSummary;
import com.bomnuocv1.domain.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetDashboardSummaryService implements GetDashboardSummaryUseCase {

    private final DashboardRepository dashboardRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardSummaryResult execute(UUID ownerId) {
        DashboardSummary summary = dashboardRepository.getSummaryByOwnerId(ownerId);

        return DashboardSummaryResult.builder()
                .totalUncollectedDebt(summary.getTotalUncollectedDebt())
                .formattedTotalDebt(summary.getFormattedTotalDebt())
                .todayPumpCount(summary.getTodayPumpCount())
                .recentTransactions(summary.getRecentTransactions().stream()
                        .map(tx -> RecentTransactionResult.builder()
                                .id(tx.getId())
                                .type(tx.getType())
                                .farmerName(tx.getFarmerName())
                                .details(tx.getDetails())
                                .amount(tx.getAmount())
                                .formattedAmount(tx.getFormattedAmount())
                                .statusBadge(tx.getStatusBadge())
                                .createdAt(tx.getCreatedAt())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
