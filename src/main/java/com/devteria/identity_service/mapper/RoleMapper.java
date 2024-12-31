package com.devteria.identity_service.mapper;

import com.devteria.identity_service.dtos.requests.RoleRequest;
import com.devteria.identity_service.dtos.responses.RoleResponse;
import com.devteria.identity_service.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
