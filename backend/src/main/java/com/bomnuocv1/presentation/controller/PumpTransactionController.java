package com.bomnuocv1.presentation.controller;

import com.bomnuocv1.application.dto.PumpTransactionResult;
import com.bomnuocv1.application.port.in.RecordPumpTransactionCommand;
import com.bomnuocv1.application.port.in.UpdatePumpTransactionCommand;
import com.bomnuocv1.application.usecase.DeletePumpTransactionUseCase;
import com.bomnuocv1.application.usecase.GetPumpTransactionsUseCase;
import com.bomnuocv1.application.usecase.RecordPumpTransactionUseCase;
import com.bomnuocv1.application.usecase.UpdatePumpTransactionUseCase;
import com.bomnuocv1.presentation.dto.request.RecordPumpTransactionRequest;
import com.bomnuocv1.presentation.dto.request.UpdatePumpTransactionRequest;
import com.bomnuocv1.presentation.dto.response.ApiResponse;
import com.bomnuocv1.presentation.dto.response.PumpTransactionResponse;
import com.bomnuocv1.presentation.mapper.PumpTransactionApiMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pump-transactions")
@RequiredArgsConstructor
public class PumpTransactionController {

    private final RecordPumpTransactionUseCase recordPumpTransactionUseCase;
    private final GetPumpTransactionsUseCase getPumpTransactionsUseCase;
    private final UpdatePumpTransactionUseCase updatePumpTransactionUseCase;
    private final DeletePumpTransactionUseCase deletePumpTransactionUseCase;
    private final PumpTransactionApiMapper mapper;

    @PostMapping
    public ResponseEntity<ApiResponse<PumpTransactionResponse>> recordTransaction(
            @Valid @RequestBody RecordPumpTransactionRequest request
    ) {
        UUID currentUserId = getCurrentUserId();
        RecordPumpTransactionCommand command = mapper.toCommand(request, currentUserId);
        PumpTransactionResult result = recordPumpTransactionUseCase.execute(command);
        PumpTransactionResponse response = mapper.toResponse(result);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Ghi nhận lượt bơm mới thành công!", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PumpTransactionResponse>>> getTransactions(
            @RequestParam(value = "farmerId", required = false) UUID farmerId,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        UUID currentUserId = getCurrentUserId();
        List<PumpTransactionResult> results = getPumpTransactionsUseCase.execute(currentUserId, farmerId, keyword);
        List<PumpTransactionResponse> response = results.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách lượt bơm thành công!", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PumpTransactionResponse>> updateTransaction(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdatePumpTransactionRequest request
    ) {
        UUID currentUserId = getCurrentUserId();
        UpdatePumpTransactionCommand command = mapper.toCommand(id, request, currentUserId);
        PumpTransactionResult result = updatePumpTransactionUseCase.execute(command);
        PumpTransactionResponse response = mapper.toResponse(result);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật lượt bơm thành công!", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTransaction(
            @PathVariable("id") UUID id
    ) {
        UUID currentUserId = getCurrentUserId();
        deletePumpTransactionUseCase.execute(id, currentUserId);
        return ResponseEntity.ok(ApiResponse.success("Xóa lượt bơm thành công!", null));
    }

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UUID) authentication.getPrincipal();
    }
}
