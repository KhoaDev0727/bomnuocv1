package com.bomnuocv1.infrastructure.cache.adapter;

import com.bomnuocv1.application.port.out.CachePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisCacheAdapter implements CachePort {

    private static final String REFRESH_TOKEN_PREFIX = "auth:refresh:";
    private static final String RATE_LIMIT_PREFIX = "rate:";

    private final StringRedisTemplate redisTemplate;

    // In-memory fallback map if Redis is not reachable during local dev
    private final Map<String, CacheEntry> inMemoryFallback = new ConcurrentHashMap<>();

    @Override
    public void saveRefreshToken(UUID userId, String refreshToken, Duration ttl) {
        String key = REFRESH_TOKEN_PREFIX + userId.toString();
        try {
            redisTemplate.opsForValue().set(key, refreshToken, ttl);
        } catch (Exception e) {
            log.warn("Redis unavailable, using in-memory fallback for saveRefreshToken: {}", e.getMessage());
            inMemoryFallback.put(key, new CacheEntry(refreshToken, System.currentTimeMillis() + ttl.toMillis()));
        }
    }

    @Override
    public boolean isRefreshTokenValid(UUID userId, String refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + userId.toString();
        try {
            String savedToken = redisTemplate.opsForValue().get(key);
            return refreshToken != null && refreshToken.equals(savedToken);
        } catch (Exception e) {
            log.warn("Redis unavailable, using in-memory fallback for isRefreshTokenValid: {}", e.getMessage());
            CacheEntry entry = inMemoryFallback.get(key);
            if (entry != null && !entry.isExpired()) {
                return refreshToken != null && refreshToken.equals(entry.value);
            }
            return false;
        }
    }

    @Override
    public void revokeRefreshToken(UUID userId) {
        String key = REFRESH_TOKEN_PREFIX + userId.toString();
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.warn("Redis unavailable, using in-memory fallback for revokeRefreshToken: {}", e.getMessage());
            inMemoryFallback.remove(key);
        }
    }

    @Override
    public boolean isRateLimited(String key, int maxAttempts, Duration window) {
        String redisKey = RATE_LIMIT_PREFIX + key;
        try {
            Long count = redisTemplate.opsForValue().increment(redisKey);
            if (count != null && count == 1) {
                redisTemplate.expire(redisKey, window);
            }
            return count != null && count > maxAttempts;
        } catch (Exception e) {
            log.warn("Redis unavailable, using in-memory fallback for rateLimit: {}", e.getMessage());
            CacheEntry entry = inMemoryFallback.get(redisKey);
            long now = System.currentTimeMillis();
            if (entry == null || entry.isExpired()) {
                inMemoryFallback.put(redisKey, new CacheEntry("1", now + window.toMillis()));
                return false;
            } else {
                int current = Integer.parseInt(entry.value) + 1;
                inMemoryFallback.put(redisKey, new CacheEntry(String.valueOf(current), entry.expiryTime));
                return current > maxAttempts;
            }
        }
    }

    private static class CacheEntry {
        final String value;
        final long expiryTime;

        CacheEntry(String value, long expiryTime) {
            this.value = value;
            this.expiryTime = expiryTime;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }
}
