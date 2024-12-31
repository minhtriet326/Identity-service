package com.devteria.identity_service.controllers;

import com.devteria.identity_service.dtos.requests.RoleRequest;
import com.devteria.identity_service.dtos.responses.ApiResponse;
import com.devteria.identity_service.dtos.responses.RoleResponse;
import com.devteria.identity_service.services.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleController {
    RoleService roleService;

    @PostMapping("/create")
    public ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder().result(roleService.create(request)).build();
    }

    @GetMapping("/getAllRoles")
    public ApiResponse<List<RoleResponse>> getAllRoles() {
        return ApiResponse.<List<RoleResponse>>builder().result(roleService.getAllRoles()).build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        roleService.delete(id);
        return ApiResponse.<Void>builder().build();
    }
}
