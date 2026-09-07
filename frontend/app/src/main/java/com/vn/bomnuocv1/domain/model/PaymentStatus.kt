package com.vn.bomnuocv1.domain.model

enum class PaymentStatus(val label: String) {
    PAID("Đã thu đủ"),
    PARTIAL("Trả một phần"),
    UNPAID("Chưa thu")
}
