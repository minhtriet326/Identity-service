package com.devteria.identity_service.services;

import com.devteria.identity_service.dtos.requests.AuthenticationRequest;
import com.devteria.identity_service.dtos.requests.IntrospectRequest;
import com.devteria.identity_service.dtos.requests.LogoutRequest;
import com.devteria.identity_service.dtos.responses.AuthenticationResponse;
import com.devteria.identity_service.dtos.responses.IntrospectResponse;
import com.devteria.identity_service.entities.InvalidatedToken;
import com.devteria.identity_service.entities.RefreshToken;
import com.devteria.identity_service.entities.User;
import com.devteria.identity_service.exceptions.AppException;
import com.devteria.identity_service.enums.ErrorCode;
import com.devteria.identity_service.repositories.InvalidatedTokenRepository;
import com.devteria.identity_service.repositories.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

//@Service
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//@Slf4j
//public class AuthenticationServiceImpl implements AuthenticationService{
//    UserRepository userRepository;
//    InvalidatedTokenRepository invalidatedTokenRepository;
//
//    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
//
//    @NonFinal
//    @Value("${jwt.signerKey}")
//    String SIGNER_KEY;
//    @Override
//    public AuthenticationResponse authenticate(AuthenticationRequest request) throws JOSEException {
//        User existingUser = userRepository.findByUsername(request.getUsername())
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//
//        boolean authenticated = passwordEncoder.matches(request.getPassword(), existingUser.getPassword());
//
//        if (!authenticated) {
//            throw new AppException(ErrorCode.UNAUTHENTICATED);
//        }
//
//        String token = generateToken(existingUser);
//
//        return AuthenticationResponse.builder()
//                .token(token)
//                .build();
//    }
//
//    @Override
//    public IntrospectResponse introspect(IntrospectRequest request)
//            throws JOSEException, ParseException {
//        String token = request.getToken();
//
//        boolean isValid = true;
//
//        try {
//            verifyToken(token);// trong hàm này có throw ex nếu token invalid
//        } catch (Exception ex) {
//            isValid = false;
//        }
//
//        return IntrospectResponse.builder()
//                .isValid(isValid)
//                .build();
//    }
//
//    @Override
//    public void logout(LogoutRequest request) throws ParseException, JOSEException {
//        SignedJWT signedJWT = verifyToken(request.getToken());
//
//        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
//                .id(signedJWT.getJWTClaimsSet().getJWTID())
//                .expiryTime(signedJWT.getJWTClaimsSet().getExpirationTime())
//                .build();
//
//        invalidatedTokenRepository.save(invalidatedToken);
//    }
//
//    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
//        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
//
//        SignedJWT signedJWT = SignedJWT.parse(token);
//
//        boolean validToken = signedJWT.verify(verifier);
//
//        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
//
//        if (!(validToken && expiryTime.after(new Date()))) {
//            throw new AppException(ErrorCode.UNAUTHENTICATED);
//        }
//
//        // Nếu token hết hạn thì đã có ex trên throw ra, nếu ko thì mới có khả năng exists trong table
//        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
//            throw new AppException(ErrorCode.UNAUTHENTICATED);
//        }
//
//        return signedJWT;
//    }
//
//    private String generateToken(User user) throws JOSEException {
//        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
//
//        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
//                .subject(user.getUsername())// đại diện cho user đăng nhập
//                .issuer("minhtriet")
//                .issueTime(new Date())
//                .expirationTime(new Date(
//                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
//                ))
//                .jwtID(UUID.randomUUID().toString())
//                .claim("scope", buildScope(user))
//                .build();
//
//        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
//
//        JWSObject jwsObject = new JWSObject(header, payload);
//
//        try {
//            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
//            return jwsObject.serialize();
//        } catch (JOSEException e) {
//            log.error("Cannot create token", e);
//            throw new RuntimeException(e);
//        }
//    }
//
//    private String buildScope(User user) {
//        StringJoiner stringJoiner = new StringJoiner(" ");
//
//        if (!user.getRoles().isEmpty()) {
//            user.getRoles().forEach(role -> {
//                stringJoiner.add("ROLE_" + role.getName());
//
//                if (!role.getPermissions().isEmpty()) {
//                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
//                }
//            }
//                    );
//        }
//
//        return stringJoiner.toString();
//    }
//}

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    RefreshTokenService refreshTokenService;

    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.maintenance-time}")
    long MAINTENANCE_TIME;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // find user
        User existingUser = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticate = passwordEncoder.matches(request.getPassword(), existingUser.getPassword());

        if (!authenticate) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(existingUser);

        String token = generateToken(existingUser);

        return AuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) {
        // verify token
        boolean isValid = true;

        try {
            verifyToken(request.getToken());
        } catch (Exception ex) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .isValid(isValid)
                .build();
    }

    @Override
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        // verify token
        SignedJWT signedJWT = verifyToken(request.getToken());

        // create InvalidatedToken
        InvalidatedToken token = InvalidatedToken.builder()
                .id(signedJWT.getJWTClaimsSet().getJWTID())
                .expiryTime(signedJWT.getJWTClaimsSet().getExpirationTime())
                .build();

        // save new InvalidatedToken
        invalidatedTokenRepository.save(token);

        // delete refresh token
        User existingUser = userRepository.findByUsername(signedJWT.getJWTClaimsSet().getSubject())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        refreshTokenService.deleteRefreshToken(existingUser.getRefreshToken().getRefreshToken());
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        // valid? expiry?
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        boolean validToken = signedJWT.verify(verifier);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        if (!(validToken && expiryTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // exist in InvalidatedToken table ?
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    @Override
    public String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("minhtriet")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(MAINTENANCE_TIME, ChronoUnit.SECONDS)
                        .toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        // sign jwt
//        JWSSigner jwsSigner = new MACSigner(SIGNER_KEY.getBytes());

        try {
//            jwsObject.sign(jwsSigner);
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner joiner = new StringJoiner(" ");

        if (!user.getRoles().isEmpty()) {
            user.getRoles().forEach(role -> {
                joiner.add("ROLE_" + role.getName());

                if (!role.getPermissions().isEmpty()) {
                    role.getPermissions().forEach(permission -> joiner.add(permission.getName()));
                }
            });
        }

        return joiner.toString();
    }
}
