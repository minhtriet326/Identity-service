package com.devteria.identity_service.services;

import java.util.List;

import com.devteria.identity_service.dtos.requests.UserCreationRequest;
import com.devteria.identity_service.dtos.requests.UserUpdateRequest;
import com.devteria.identity_service.dtos.responses.UserResponse;

public interface UserService {
  UserResponse createUser(UserCreationRequest request);

  List<UserResponse> getAllUsers();

  UserResponse getUserById(String id);

  UserResponse getMyInfo();

  UserResponse updateUser(String id, UserUpdateRequest request);

  String deleteUser(String id);
}
