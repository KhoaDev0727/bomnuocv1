package com.bomnuocv1.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class DashboardSummaryResult {

    private final BigDecimal totalUncollectedDebt;
    private final String formattedTotalDebt;
    private final long todayPumpCount;
    private final List<RecentTransactionResult> recentTransactions;
}
