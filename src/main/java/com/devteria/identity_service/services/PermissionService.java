package com.devteria.identity_service.services;

import com.devteria.identity_service.dtos.requests.PermissionRequest;
import com.devteria.identity_service.dtos.responses.PermissionResponse;

import java.util.List;

public interface PermissionService {
    PermissionResponse create(PermissionRequest request);

    List<PermissionResponse> getAllPermissions();

    void delete(String id);
}
