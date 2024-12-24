package com.devteria.identity_service.mapper;

import com.devteria.identity_service.dtos.requests.PermissionRequest;
import com.devteria.identity_service.dtos.responses.PermissionResponse;
import com.devteria.identity_service.entities.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
