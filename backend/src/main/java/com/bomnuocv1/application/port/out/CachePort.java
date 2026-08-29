package com.bomnuocv1.application.port.out;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

public interface CachePort {

    void saveRefreshToken(UUID userId, String refreshToken, Duration ttl);

    boolean isRefreshTokenValid(UUID userId, String refreshToken);

    void revokeRefreshToken(UUID userId);

    boolean isRateLimited(String key, int maxAttempts, Duration window);
}
