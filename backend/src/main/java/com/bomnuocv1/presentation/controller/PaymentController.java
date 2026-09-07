package com.bomnuocv1.presentation.controller;

import com.bomnuocv1.application.dto.PaymentResult;
import com.bomnuocv1.application.port.in.RecordPaymentCommand;
import com.bomnuocv1.application.usecase.GetPaymentsUseCase;
import com.bomnuocv1.application.usecase.RecordPaymentUseCase;
import com.bomnuocv1.presentation.dto.request.RecordPaymentRequest;
import com.bomnuocv1.presentation.dto.response.ApiResponse;
import com.bomnuocv1.presentation.dto.response.PaymentResponse;
import com.bomnuocv1.presentation.mapper.PaymentApiMapper;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final RecordPaymentUseCase recordPaymentUseCase;
    private final GetPaymentsUseCase getPaymentsUseCase;
    private final PaymentApiMapper mapper;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> recordPayment(
            @Valid @RequestBody RecordPaymentRequest request
    ) {
        UUID currentUserId = getCurrentUserId();
        RecordPaymentCommand command = mapper.toCommand(request, currentUserId);
        PaymentResult result = recordPaymentUseCase.execute(command);
        PaymentResponse response = mapper.toResponse(result);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Ghi nhận thanh toán thành công!", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getPayments(
            @RequestParam(value = "farmerId", required = false) UUID farmerId,
            @RequestParam(value = "transactionId", required = false) UUID transactionId
    ) {
        UUID currentUserId = getCurrentUserId();
        List<PaymentResult> results = getPaymentsUseCase.execute(currentUserId, farmerId, transactionId);
        List<PaymentResponse> response = results.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Lấy lịch sử thanh toán thành công!", response));
    }

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UUID) authentication.getPrincipal();
    }
}
