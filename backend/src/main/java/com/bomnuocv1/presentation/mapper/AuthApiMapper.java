package com.bomnuocv1.presentation.mapper;

import com.bomnuocv1.application.dto.AuthResult;
import com.bomnuocv1.application.dto.TokenResult;
import com.bomnuocv1.application.dto.UserResult;
import com.bomnuocv1.application.port.in.LoginWithPinCommand;
import com.bomnuocv1.application.port.in.RefreshTokenCommand;
import com.bomnuocv1.application.port.in.RegisterUserCommand;
import com.bomnuocv1.presentation.dto.request.LoginWithPinRequest;
import com.bomnuocv1.presentation.dto.request.RefreshTokenRequest;
import com.bomnuocv1.presentation.dto.request.RegisterRequest;
import com.bomnuocv1.presentation.dto.response.AuthResponse;
import com.bomnuocv1.presentation.dto.response.TokenResponse;
import com.bomnuocv1.presentation.dto.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthApiMapper {

    public RegisterUserCommand toCommand(RegisterRequest request) {
        if (request == null) return null;
        return RegisterUserCommand.builder()
                .phoneNumber(request.getPhoneNumber())
                .pinCode(request.getPinCode())
                .fullName(request.getFullName())
                .roleCode(request.getRoleCode())
                .otpCode(request.getOtpCode())
                .firebaseIdToken(request.getFirebaseIdToken())
                .build();
    }

    public LoginWithPinCommand toCommand(LoginWithPinRequest request) {
        if (request == null) return null;
        return LoginWithPinCommand.builder()
                .phoneNumber(request.getPhoneNumber())
                .pinCode(request.getPinCode())
                .build();
    }

    public RefreshTokenCommand toCommand(RefreshTokenRequest request) {
        if (request == null) return null;
        return RefreshTokenCommand.builder()
                .refreshToken(request.getRefreshToken())
                .build();
    }

    public UserResponse toResponse(UserResult result) {
        if (result == null) return null;
        return UserResponse.builder()
                .id(result.getId())
                .phoneNumber(result.getPhoneNumber())
                .fullName(result.getFullName())
                .roleCode(result.getRoleCode())
                .roleName(result.getRoleName())
                .active(result.isActive())
                .createdAt(result.getCreatedAt())
                .build();
    }

    public TokenResponse toResponse(TokenResult result) {
        if (result == null) return null;
        return TokenResponse.builder()
                .accessToken(result.getAccessToken())
                .refreshToken(result.getRefreshToken())
                .tokenType(result.getTokenType())
                .expiresInMs(result.getExpiresInMs())
                .build();
    }

    public AuthResponse toResponse(AuthResult result) {
        if (result == null) return null;
        return AuthResponse.builder()
                .user(toResponse(result.getUser()))
                .tokens(toResponse(result.getTokens()))
                .build();
    }
}
