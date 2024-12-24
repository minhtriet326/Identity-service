package com.devteria.identity_service.services;

import com.devteria.identity_service.dtos.requests.RoleRequest;
import com.devteria.identity_service.dtos.responses.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse create(RoleRequest request);
    List<RoleResponse> getAllRoles();
    void delete(String id);
}
