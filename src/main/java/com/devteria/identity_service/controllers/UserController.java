package com.devteria.identity_service.controllers;

import com.devteria.identity_service.dtos.requests.UserCreationRequest;
import com.devteria.identity_service.dtos.requests.UserUpdateRequest;
import com.devteria.identity_service.dtos.responses.ApiResponse;
import com.devteria.identity_service.dtos.responses.UserResponse;
import com.devteria.identity_service.services.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {
    UserService userService;

    @PostMapping("/createUser")
    public ApiResponse<UserResponse> createUserHandler(@Valid @RequestBody UserCreationRequest request) {
        ApiResponse<UserResponse> userApiResponse = new ApiResponse<>();

        userApiResponse.setResult(userService.createUser(request));

        return userApiResponse;
    }

    @PreAuthorize("hasRole('APPROVE_POST')")
    @GetMapping("/getAllUsers")
    public List<UserResponse> getAllUsersHandler() {
        return userService.getAllUsers();
    }

    @PostAuthorize("returnObject.username == authentication.name")
    @GetMapping("/getUserById/{userId}")
    public UserResponse getUserByIdHandler(@PathVariable String userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/getMyInfo")
    public UserResponse getMyInfo() {
        return userService.getMyInfo();
    }

    @PostAuthorize("returnObject.username == authentication.username")
    @PutMapping("/updateUser/{userId}")
    public UserResponse updateUserHandler(@PathVariable String userId,
                                  @Valid @RequestBody UserUpdateRequest request) {
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/deleteUser/{userId}")
    public String deleteUserHandler(@PathVariable String userId) {
        return userService.deleteUser(userId);
    }
}
