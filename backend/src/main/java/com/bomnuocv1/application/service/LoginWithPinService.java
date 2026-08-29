package com.bomnuocv1.application.service;

import com.bomnuocv1.application.dto.AuthResult;
import com.bomnuocv1.application.dto.TokenResult;
import com.bomnuocv1.application.dto.UserResult;
import com.bomnuocv1.application.port.in.LoginWithPinCommand;
import com.bomnuocv1.application.port.out.CachePort;
import com.bomnuocv1.application.port.out.PasswordEncoderPort;
import com.bomnuocv1.application.port.out.TokenProviderPort;
import com.bomnuocv1.application.usecase.LoginWithPinUseCase;
import com.bomnuocv1.domain.entity.User;
import com.bomnuocv1.domain.exception.InvalidPinException;
import com.bomnuocv1.domain.exception.UnauthorizedException;
import com.bomnuocv1.domain.exception.UserNotFoundException;
import com.bomnuocv1.domain.repository.UserRepository;
import com.bomnuocv1.domain.valueobject.PhoneNumber;
import com.bomnuocv1.domain.valueobject.PinCode;
import com.bomnuocv1.domain.valueobject.TokenPair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginWithPinService implements LoginWithPinUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenProviderPort tokenProviderPort;
    private final CachePort cachePort;

    @Override
    @Transactional(readOnly = true)
    public AuthResult execute(LoginWithPinCommand command) {
        PhoneNumber phoneNumber = PhoneNumber.of(command.getPhoneNumber());
        PinCode pinCode = PinCode.of(command.getPinCode());

        // 1. Find user by phone number
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UserNotFoundException("Số điện thoại chưa được đăng ký trong hệ thống."));

        // 2. Check if user is active
        if (!user.isActive()) {
            throw new UnauthorizedException("Tài khoản của bạn đã bị khóa hoặc vô hiệu hóa.");
        }

        // 3. Verify PIN
        boolean matches = passwordEncoderPort.matches(pinCode, user.getPinHash());
        if (!matches) {
            log.warn("Invalid PIN attempt for phone: {}", phoneNumber.getValue());
            throw new InvalidPinException("Mã PIN không chính xác.");
        }

        // 4. Generate JWT Tokens
        TokenPair tokenPair = tokenProviderPort.generateTokenPair(user);

        // 5. Save refresh token to Redis for session control
        long refreshTtlMs = tokenProviderPort.getRefreshTokenExpirationMs();
        cachePort.saveRefreshToken(user.getId(), tokenPair.getRefreshToken(), Duration.ofMillis(refreshTtlMs));

        log.info("User {} logged in successfully with PIN.", user.getPhoneNumber().getValue());

        // 6. Return AuthResult
        UserResult userResult = UserResult.builder()
                .id(user.getId())
                .phoneNumber(user.getPhoneNumber().getValue())
                .fullName(user.getFullName())
                .roleCode(user.getRole().getCode())
                .roleName(user.getRole().getName())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();

        TokenResult tokenResult = TokenResult.builder()
                .accessToken(tokenPair.getAccessToken())
                .refreshToken(tokenPair.getRefreshToken())
                .tokenType(tokenPair.getTokenType())
                .expiresInMs(tokenPair.getExpiresInMs())
                .build();

        return AuthResult.builder()
                .user(userResult)
                .tokens(tokenResult)
                .build();
    }
}
