package com.devteria.identity_service.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.devteria.identity_service.dtos.requests.PermissionRequest;
import com.devteria.identity_service.dtos.responses.ApiResponse;
import com.devteria.identity_service.dtos.responses.PermissionResponse;
import com.devteria.identity_service.services.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/v1/permission")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
  PermissionService permissionService;

  @PostMapping("/create")
  public ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request) {
    return ApiResponse.<PermissionResponse>builder()
        .result(permissionService.create(request))
        .build();
  }

  @GetMapping("/getAllPermissions")
  public ApiResponse<List<PermissionResponse>> getAllPermissions() {
    return ApiResponse.<List<PermissionResponse>>builder()
        .result(permissionService.getAllPermissions())
        .build();
  }

  @DeleteMapping("/deletePermission/{id}")
  public ApiResponse<Void> deletePermission(@PathVariable String id) {
    permissionService.delete(id);

    return ApiResponse.<Void>builder().build();
  }
}
