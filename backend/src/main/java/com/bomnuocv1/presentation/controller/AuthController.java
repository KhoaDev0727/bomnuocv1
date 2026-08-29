package com.bomnuocv1.presentation.controller;

import com.bomnuocv1.application.dto.AuthResult;
import com.bomnuocv1.application.dto.TokenResult;
import com.bomnuocv1.application.dto.UserResult;
import com.bomnuocv1.application.port.out.CachePort;
import com.bomnuocv1.application.usecase.GetCurrentUserUseCase;
import com.bomnuocv1.application.usecase.LoginWithPinUseCase;
import com.bomnuocv1.application.usecase.RefreshTokenUseCase;
import com.bomnuocv1.application.usecase.RegisterUserUseCase;
import com.bomnuocv1.presentation.dto.request.LoginWithPinRequest;
import com.bomnuocv1.presentation.dto.request.RefreshTokenRequest;
import com.bomnuocv1.presentation.dto.request.RegisterRequest;
import com.bomnuocv1.presentation.dto.response.ApiResponse;
import com.bomnuocv1.presentation.dto.response.AuthResponse;
import com.bomnuocv1.presentation.dto.response.TokenResponse;
import com.bomnuocv1.presentation.dto.response.UserResponse;
import com.bomnuocv1.presentation.mapper.AuthApiMapper;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginWithPinUseCase loginWithPinUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;
    private final CachePort cachePort;
    private final AuthApiMapper mapper;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResult result = registerUserUseCase.execute(mapper.toCommand(request));
        AuthResponse response = mapper.toResponse(result);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Đăng ký tài khoản thành công!", response));
    }

    @PostMapping("/login-pin")
    public ResponseEntity<ApiResponse<AuthResponse>> loginWithPin(@Valid @RequestBody LoginWithPinRequest request) {
        AuthResult result = loginWithPinUseCase.execute(mapper.toCommand(request));
        AuthResponse response = mapper.toResponse(result);
        return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công!", response));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        TokenResult result = refreshTokenUseCase.execute(mapper.toCommand(request));
        TokenResponse response = mapper.toResponse(result);
        return ResponseEntity.ok(ApiResponse.success("Làm mới phiên đăng nhập thành công!", response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID currentUserId = (UUID) authentication.getPrincipal();
        UserResult result = getCurrentUserUseCase.execute(currentUserId);
        UserResponse response = mapper.toResponse(result);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin tài khoản thành công!", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UUID currentUserId) {
            cachePort.revokeRefreshToken(currentUserId);
        }
        return ResponseEntity.ok(ApiResponse.success("Đăng xuất thành công!", null));
    }
}
