package com.devteria.identity_service.configurations;

import com.devteria.identity_service.dtos.requests.IntrospectRequest;
import com.devteria.identity_service.dtos.responses.IntrospectResponse;
import com.devteria.identity_service.exceptions.AppException;
import com.devteria.identity_service.services.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;

//@Component
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@RequiredArgsConstructor
//public class CustomJwtDecoder implements JwtDecoder {
//    @Value("${jwt.signerKey}")
//    String SIGNER_KEY;
//
//    final AuthenticationService authenticationService;
//
//    @Override
//    public Jwt decode(String token) throws JwtException {
//        try {
//            var response = authenticationService.introspect(IntrospectRequest.builder()
//                            .token(token)
//                    .build());
//
//            if (!response.isValid()) {
//                throw new JwtException("Token invalid");
//            }
//
//        } catch (ParseException | JOSEException e) {
//            throw new JwtException(e.getMessage());
//        }
//
//        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
//
//        NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder
//                .withSecretKey(secretKeySpec)
//                .macAlgorithm(MacAlgorithm.HS512)
//                .build();
//
//        return nimbusJwtDecoder.decode(token);
//    }
//}

//@Component
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//@RequiredArgsConstructor
//public class CustomJwtDecoder implements JwtDecoder {
//    @NonFinal
//    @Value("${jwt.signerKey}")
//    String SIGNER_KEY;
//
//    AuthenticationService authenticationService;
//
//    @Override
//    public Jwt decode(String token) throws JwtException {
//        // verify token
//        // valid ?
//        // expiry?
//        try {
//            IntrospectResponse response = authenticationService.introspect(
//                    IntrospectRequest.builder()
//                            .token(token)
//                            .build()
//            );
//
//            if (!response.isValid()) {
//                throw new JwtException("Invalid Token"); // bị "wrap" (bọc) nó trong AuthenticationServiceException
//            }
//
//        } catch (JOSEException | ParseException e) {
//            throw new JwtException(e.getMessage());
//        }
//
//        // create and return nimbusJwtDecoder
//        // check signature
//        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
//        return NimbusJwtDecoder
//                .withSecretKey(secretKeySpec)
//                .macAlgorithm(MacAlgorithm.HS512)
//                .build().decode(token);
//    }
//}

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CustomJwtDecoder implements JwtDecoder {
    AuthenticationService authenticationService;

    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGNER_KEY;

    @Override
    public Jwt decode(String token) throws JwtException {
        // valid token -> introspect
        try {
            IntrospectResponse introspectResponse = authenticationService.introspect(
                    IntrospectRequest.builder()
                            .token(token)
                            .build()
            );

            if (!introspectResponse.isValid()) {
                throw new JwtException("Invalid token");
            }
        } catch (JOSEException | ParseException e) {
            throw new JwtException(e.getMessage());
        }

        // create and return NimbusJwtDecoder
        // check signature
        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");

        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build().decode(token);
    }
}

