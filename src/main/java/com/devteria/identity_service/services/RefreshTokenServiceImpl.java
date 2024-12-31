package com.devteria.identity_service.services;

import com.devteria.identity_service.entities.RefreshToken;
import com.devteria.identity_service.entities.User;
import com.devteria.identity_service.enums.ErrorCode;
import com.devteria.identity_service.exceptions.AppException;
import com.devteria.identity_service.repositories.RefreshTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RefreshTokenServiceImpl implements RefreshTokenService {
    RefreshTokenRepository refreshTokenRepository;

    @NonFinal
    @Value("${jwt.refreshable-time}")
    long REFRESHABLE_TIME;

    @Override
    public RefreshToken createRefreshToken(User user) {
        if (user.getRefreshToken() == null) {

            RefreshToken refreshToken =
                    RefreshToken.builder()
                            .refreshToken(UUID.randomUUID().toString())
                            .expiryTime(
                                    new Date(Instant.now().plus(REFRESHABLE_TIME, ChronoUnit.SECONDS).toEpochMilli()))
                            .user(user)
                            .build();

            refreshTokenRepository.save(refreshToken);

            return refreshTokenRepository.save(refreshToken);
        }

        return user.getRefreshToken();
    }

    @Override
    public RefreshToken verifyRefreshToken(String refreshToken) {
        RefreshToken existingRefreshToken =
                refreshTokenRepository
                        .findByRefreshToken(refreshToken)
                        .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        if (!existingRefreshToken.getExpiryTime().after(Date.from(Instant.now()))) {
            refreshTokenRepository.delete(existingRefreshToken);
            throw new AppException(ErrorCode.SESSION_EXPIRED);
        }

        return existingRefreshToken;
    }

    @Override
    public void deleteRefreshToken(String refreshToken) {
        RefreshToken existingRefreshToken =
                refreshTokenRepository
                        .findByRefreshToken(refreshToken)
                        .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        refreshTokenRepository.delete(existingRefreshToken);
    }
}
