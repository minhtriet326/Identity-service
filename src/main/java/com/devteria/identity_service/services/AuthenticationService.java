package com.devteria.identity_service.services;

import java.text.ParseException;

import com.devteria.identity_service.dtos.requests.AuthenticationRequest;
import com.devteria.identity_service.dtos.requests.IntrospectRequest;
import com.devteria.identity_service.dtos.requests.LogoutRequest;
import com.devteria.identity_service.dtos.responses.AuthenticationResponse;
import com.devteria.identity_service.dtos.responses.IntrospectResponse;
import com.devteria.identity_service.entities.User;
import com.nimbusds.jose.JOSEException;

public interface AuthenticationService {
  AuthenticationResponse authenticate(AuthenticationRequest request) throws JOSEException;

  IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;

  void logout(LogoutRequest request) throws ParseException, JOSEException;

  String generateToken(User user);
}

// public interface AuthenticationService {
//    AuthenticationResponse authenticate(AuthenticationRequest request) throws JOSEException;
//    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
//    void logout(LogoutRequest request) throws ParseException, JOSEException;
// }
