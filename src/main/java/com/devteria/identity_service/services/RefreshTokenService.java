package com.devteria.identity_service.services;

import com.devteria.identity_service.entities.RefreshToken;
import com.devteria.identity_service.entities.User;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);

    RefreshToken verifyRefreshToken(String refreshToken);

    void deleteRefreshToken(String refreshToken);
}
