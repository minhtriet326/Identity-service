package com.devteria.identity_service.services;

import java.util.List;

import com.devteria.identity_service.dtos.requests.PermissionRequest;
import com.devteria.identity_service.dtos.responses.PermissionResponse;

public interface PermissionService {
  PermissionResponse create(PermissionRequest request);

  List<PermissionResponse> getAllPermissions();

  void delete(String id);
}
