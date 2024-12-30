package com.devteria.identity_service.controllers;

import java.text.ParseException;

import org.springframework.web.bind.annotation.*;

import com.devteria.identity_service.dtos.requests.AuthenticationRequest;
import com.devteria.identity_service.dtos.requests.IntrospectRequest;
import com.devteria.identity_service.dtos.requests.LogoutRequest;
import com.devteria.identity_service.dtos.requests.RefreshRequest;
import com.devteria.identity_service.dtos.responses.ApiResponse;
import com.devteria.identity_service.dtos.responses.AuthenticationResponse;
import com.devteria.identity_service.dtos.responses.IntrospectResponse;
import com.devteria.identity_service.entities.RefreshToken;
import com.devteria.identity_service.entities.User;
import com.devteria.identity_service.services.AuthenticationService;
import com.devteria.identity_service.services.RefreshTokenService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/v1/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationController {
  AuthenticationService authenticationService;
  RefreshTokenService refreshTokenService;

  @PostMapping("/login")
  public ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request)
      throws JOSEException {
    return ApiResponse.<AuthenticationResponse>builder()
        .result(authenticationService.authenticate(request))
        .build();
  }

  @PostMapping("/introspect")
  public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
      throws ParseException, JOSEException {
    return ApiResponse.<IntrospectResponse>builder()
        .result(authenticationService.introspect(request))
        .build();
  }

  @PostMapping("/logout")
  public ApiResponse<Void> logout(@RequestBody LogoutRequest request)
      throws ParseException, JOSEException {
    authenticationService.logout(request);
    return ApiResponse.<Void>builder().build();
  }

  @PostMapping("/refresh")
  public ApiResponse<AuthenticationResponse> refreshAccessToken(
      @RequestBody RefreshRequest request) {
    // check RefreshToken
    RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());

    // create accessToken
    User existingUser = refreshToken.getUser();

    String accessToken = authenticationService.generateToken(existingUser);

    return ApiResponse.<AuthenticationResponse>builder()
        .result(
            AuthenticationResponse.builder()
                .refreshToken(refreshToken.getRefreshToken())
                .accessToken(accessToken)
                .build())
        .build();
  }
}

// @RestController
// @RequestMapping("/api/v1/auth")
// @RequiredArgsConstructor
// @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
// public class AuthenticationController {
//    AuthenticationService authenticationService;
//
//    @PostMapping("/login")
//    public ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request)
//            throws JOSEException {
//        return ApiResponse.<AuthenticationResponse>builder()
//                .result(authenticationService.authenticate(request))
//                .build();
//    }
//
//    @PostMapping("/introspect")
//    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
//            throws JOSEException, ParseException {
//        return ApiResponse.<IntrospectResponse>builder()
//                .result(authenticationService.introspect(request))
//                .build();
//    }
//
//    @PostMapping("/logout")
//    public ApiResponse<Void> logout(@RequestBody LogoutRequest request)
//            throws JOSEException, ParseException {
//
//        authenticationService.logout(request);
//
//        return ApiResponse.<Void>builder()
//                .build();
//    }
// }
