package com.bomnuocv1.domain.entity;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class RecentTransaction {

    private final UUID id;
    private final String type; // PUMP or PAYMENT
    private final String farmerName;
    private final String details; // e.g. "Bơm 2.5 công • 08:30 sáng" or "Thu nợ cũ • Hôm qua"
    private final BigDecimal amount; // positive for pump (+500.000), negative for payment (-1.200.000) or vice versa
    private final String formattedAmount; // e.g. "+ 500.000đ" or "- 1.200.000đ"
    private final String statusBadge; // "Ghi nợ" or "Đã thu"
    private final Instant createdAt;
}
