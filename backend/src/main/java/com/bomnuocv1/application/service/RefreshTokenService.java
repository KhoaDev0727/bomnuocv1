package com.bomnuocv1.application.service;

import com.bomnuocv1.application.dto.TokenResult;
import com.bomnuocv1.application.port.in.RefreshTokenCommand;
import com.bomnuocv1.application.port.out.CachePort;
import com.bomnuocv1.application.port.out.TokenProviderPort;
import com.bomnuocv1.application.usecase.RefreshTokenUseCase;
import com.bomnuocv1.domain.entity.User;
import com.bomnuocv1.domain.exception.InvalidTokenException;
import com.bomnuocv1.domain.exception.UnauthorizedException;
import com.bomnuocv1.domain.repository.UserRepository;
import com.bomnuocv1.domain.valueobject.TokenPair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenUseCase {

    private final TokenProviderPort tokenProviderPort;
    private final CachePort cachePort;
    private final UserRepository userRepository;

    @Override
    public TokenResult execute(RefreshTokenCommand command) {
        String refreshToken = command.getRefreshToken();
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            throw new InvalidTokenException("Refresh token không được để trống.");
        }

        // 1. Validate JWT structure and signature
        if (!tokenProviderPort.validateRefreshToken(refreshToken)) {
            throw new InvalidTokenException("Refresh token không hợp lệ hoặc đã hết hạn.");
        }

        // 2. Extract user ID
        UUID userId = tokenProviderPort.extractUserIdFromRefreshToken(refreshToken);

        // 3. Verify with Redis session whitelist
        boolean validInCache = cachePort.isRefreshTokenValid(userId, refreshToken);
        if (!validInCache) {
            throw new InvalidTokenException("Phiên đăng nhập đã hết hạn hoặc bị thu hồi từ thiết bị khác.");
        }

        // 4. Fetch User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("Người dùng không tồn tại."));

        if (!user.isActive()) {
            throw new UnauthorizedException("Tài khoản của bạn đã bị vô hiệu hóa.");
        }

        // 5. Generate New Tokens
        TokenPair newTokenPair = tokenProviderPort.generateTokenPair(user);

        // 6. Update Redis with new Refresh Token
        long refreshTtlMs = tokenProviderPort.getRefreshTokenExpirationMs();
        cachePort.saveRefreshToken(user.getId(), newTokenPair.getRefreshToken(), Duration.ofMillis(refreshTtlMs));

        log.info("Refreshed tokens successfully for user: {}", user.getPhoneNumber().getValue());

        return TokenResult.builder()
                .accessToken(newTokenPair.getAccessToken())
                .refreshToken(newTokenPair.getRefreshToken())
                .tokenType(newTokenPair.getTokenType())
                .expiresInMs(newTokenPair.getExpiresInMs())
                .build();
    }
}
