package com.bomnuocv1.infrastructure.persistence.adapter;

import com.bomnuocv1.domain.entity.DashboardSummary;
import com.bomnuocv1.domain.entity.RecentTransaction;
import com.bomnuocv1.domain.repository.DashboardRepository;
import com.bomnuocv1.infrastructure.persistence.entity.FarmerJpaEntity;
import com.bomnuocv1.infrastructure.persistence.entity.PaymentJpaEntity;
import com.bomnuocv1.infrastructure.persistence.entity.PumpTransactionJpaEntity;
import com.bomnuocv1.infrastructure.persistence.repository.FarmerJpaRepository;
import com.bomnuocv1.infrastructure.persistence.repository.PaymentJpaRepository;
import com.bomnuocv1.infrastructure.persistence.repository.PumpTransactionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DashboardRepositoryAdapter implements DashboardRepository {

    private final PumpTransactionJpaRepository pumpTransactionJpaRepository;
    private final PaymentJpaRepository paymentJpaRepository;
    private final FarmerJpaRepository farmerJpaRepository;

    @Override
    public DashboardSummary getSummaryByOwnerId(UUID ownerId) {
        LocalDate today = LocalDate.now();

        BigDecimal totalDue = pumpTransactionJpaRepository.sumTotalDueByOwnerId(ownerId);
        BigDecimal totalPaid = paymentJpaRepository.sumTotalPaidByOwnerId(ownerId);
        BigDecimal outstandingDebt = totalDue.subtract(totalPaid);
        if (outstandingDebt.compareTo(BigDecimal.ZERO) < 0) {
            outstandingDebt = BigDecimal.ZERO;
        }

        long todayPumpCount = pumpTransactionJpaRepository.countTodayPumpsByOwnerId(ownerId, today);

        // Fetch farmers map for quick name resolution
        Map<UUID, String> farmerNames = farmerJpaRepository.findActiveByOwnerId(ownerId).stream()
                .collect(Collectors.toMap(FarmerJpaEntity::getId, FarmerJpaEntity::getFullName, (k1, k2) -> k1));

        List<PumpTransactionJpaEntity> pumpList = pumpTransactionJpaRepository.findTop10ByOwnerId(ownerId);
        List<PaymentJpaEntity> paymentList = paymentJpaRepository.findTop10ByOwnerId(ownerId);

        List<RecentTransaction> recentList = new ArrayList<>();

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMAN);
        DecimalFormat currencyFormat = new DecimalFormat("#,###", symbols);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.forLanguageTag("vi-VN"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM", Locale.forLanguageTag("vi-VN"));

        for (PumpTransactionJpaEntity pump : pumpList) {
            String farmerName = farmerNames.getOrDefault(pump.getFarmerId(), "Nông dân");
            String timeStr = formatInstant(pump.getCreatedAt(), timeFormatter, dateFormatter);
            String details = "Bơm " + formatQuantity(pump.getQuantity()) + " " + pump.getQuantityUnit() + " • " + timeStr;
            String formattedAmount = "+ " + currencyFormat.format(pump.getAmountDue()) + "đ";

            recentList.add(RecentTransaction.builder()
                    .id(pump.getId())
                    .type("PUMP")
                    .farmerName(farmerName)
                    .details(details)
                    .amount(pump.getAmountDue())
                    .formattedAmount(formattedAmount)
                    .statusBadge("Ghi nợ")
                    .createdAt(pump.getCreatedAt())
                    .build());
        }

        for (PaymentJpaEntity payment : paymentList) {
            String farmerName = farmerNames.getOrDefault(payment.getFarmerId(), "Nông dân");
            String timeStr = formatInstant(payment.getCreatedAt(), timeFormatter, dateFormatter);
            String noteText = payment.getNote() != null && !payment.getNote().trim().isEmpty()
                    ? payment.getNote()
                    : "Thu nợ cũ";
            String details = noteText + " • " + timeStr;
            String formattedAmount = "- " + currencyFormat.format(payment.getAmount()) + "đ";

            recentList.add(RecentTransaction.builder()
                    .id(payment.getId())
                    .type("PAYMENT")
                    .farmerName(farmerName)
                    .details(details)
                    .amount(payment.getAmount().negate())
                    .formattedAmount(formattedAmount)
                    .statusBadge("Đã thu")
                    .createdAt(payment.getCreatedAt())
                    .build());
        }

        // Sort descending by createdAt
        recentList.sort(Comparator.comparing(RecentTransaction::getCreatedAt).reversed());

        // Limit to 10 most recent
        List<RecentTransaction> topRecent = recentList.stream().limit(10).collect(Collectors.toList());

        String formattedTotalDebt = currencyFormat.format(outstandingDebt) + "đ";

        return DashboardSummary.builder()
                .totalUncollectedDebt(outstandingDebt)
                .formattedTotalDebt(formattedTotalDebt)
                .todayPumpCount(todayPumpCount)
                .recentTransactions(topRecent)
                .build();
    }

    private String formatQuantity(BigDecimal qty) {
        if (qty == null) return "0";
        if (qty.stripTrailingZeros().scale() <= 0) {
            return String.valueOf(qty.intValue());
        }
        return qty.toString();
    }

    private String formatInstant(Instant instant, DateTimeFormatter timeFormatter, DateTimeFormatter dateFormatter) {
        if (instant == null) return "Hôm nay";
        LocalDate date = LocalDate.ofInstant(instant, ZoneId.systemDefault());
        LocalDate today = LocalDate.now();
        if (date.isEqual(today)) {
            return timeFormatter.format(instant.atZone(ZoneId.systemDefault()));
        } else if (date.isEqual(today.minusDays(1))) {
            return "Hôm qua";
        } else {
            return dateFormatter.format(date);
        }
    }
}
