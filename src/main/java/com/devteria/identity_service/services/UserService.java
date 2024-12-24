package com.devteria.identity_service.services;

import com.devteria.identity_service.dtos.requests.UserCreationRequest;
import com.devteria.identity_service.dtos.requests.UserUpdateRequest;
import com.devteria.identity_service.dtos.responses.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreationRequest request);
    List<UserResponse> getAllUsers();
    UserResponse getUserById(String id);
    UserResponse getMyInfo();
    UserResponse updateUser(String id, UserUpdateRequest request);
    String deleteUser(String id);
}
