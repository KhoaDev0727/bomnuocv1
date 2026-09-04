package com.bomnuocv1.presentation.controller;

import com.bomnuocv1.application.dto.FarmerResult;
import com.bomnuocv1.application.port.in.CreateFarmerCommand;
import com.bomnuocv1.application.port.in.UpdateFarmerCommand;
import com.bomnuocv1.application.usecase.CreateFarmerUseCase;
import com.bomnuocv1.application.usecase.DeleteFarmerUseCase;
import com.bomnuocv1.application.usecase.GetFarmersUseCase;
import com.bomnuocv1.application.usecase.UpdateFarmerUseCase;
import com.bomnuocv1.presentation.dto.request.CreateFarmerRequest;
import com.bomnuocv1.presentation.dto.request.UpdateFarmerRequest;
import com.bomnuocv1.presentation.dto.response.ApiResponse;
import com.bomnuocv1.presentation.dto.response.FarmerResponse;
import com.bomnuocv1.presentation.mapper.FarmerApiMapper;
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
@RequestMapping("/api/v1/farmers")
@RequiredArgsConstructor
public class FarmerController {

    private final GetFarmersUseCase getFarmersUseCase;
    private final CreateFarmerUseCase createFarmerUseCase;
    private final UpdateFarmerUseCase updateFarmerUseCase;
    private final DeleteFarmerUseCase deleteFarmerUseCase;
    private final FarmerApiMapper mapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<FarmerResponse>>> getFarmers(
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        UUID currentUserId = getCurrentUserId();
        List<FarmerResult> results = getFarmersUseCase.execute(currentUserId, keyword);
        List<FarmerResponse> response = results.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách nông dân thành công!", response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FarmerResponse>> createFarmer(
            @Valid @RequestBody CreateFarmerRequest request
    ) {
        UUID currentUserId = getCurrentUserId();
        CreateFarmerCommand command = mapper.toCommand(request, currentUserId);
        FarmerResult result = createFarmerUseCase.execute(command);
        FarmerResponse response = mapper.toResponse(result);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Thêm nông dân mới thành công!", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FarmerResponse>> updateFarmer(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateFarmerRequest request
    ) {
        UUID currentUserId = getCurrentUserId();
        UpdateFarmerCommand command = mapper.toCommand(id, request, currentUserId);
        FarmerResult result = updateFarmerUseCase.execute(command);
        FarmerResponse response = mapper.toResponse(result);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thông tin nông dân thành công!", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFarmer(
            @PathVariable("id") UUID id
    ) {
        UUID currentUserId = getCurrentUserId();
        deleteFarmerUseCase.execute(id, currentUserId);
        return ResponseEntity.ok(ApiResponse.success("Xóa hồ sơ nông dân thành công!", null));
    }

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UUID) authentication.getPrincipal();
    }
}
