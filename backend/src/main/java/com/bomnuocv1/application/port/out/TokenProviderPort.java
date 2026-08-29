package com.bomnuocv1.application.port.out;

import com.bomnuocv1.domain.entity.User;
import com.bomnuocv1.domain.valueobject.TokenPair;

import java.util.UUID;

public interface TokenProviderPort {

    TokenPair generateTokenPair(User user);

    boolean validateAccessToken(String token);

    boolean validateRefreshToken(String token);

    UUID extractUserIdFromAccessToken(String token);

    UUID extractUserIdFromRefreshToken(String token);

    long getAccessTokenExpirationMs();

    long getRefreshTokenExpirationMs();
}
