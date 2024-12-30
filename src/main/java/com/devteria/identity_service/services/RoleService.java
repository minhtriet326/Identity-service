package com.devteria.identity_service.services;

import java.util.List;

import com.devteria.identity_service.dtos.requests.RoleRequest;
import com.devteria.identity_service.dtos.responses.RoleResponse;

public interface RoleService {
  RoleResponse create(RoleRequest request);

  List<RoleResponse> getAllRoles();

  void delete(String id);
}
