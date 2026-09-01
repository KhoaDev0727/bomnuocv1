package com.bomnuocv1.domain.entity;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class DashboardSummary {

    private final BigDecimal totalUncollectedDebt;
    private final String formattedTotalDebt;
    private final long todayPumpCount;
    private final List<RecentTransaction> recentTransactions;
}
