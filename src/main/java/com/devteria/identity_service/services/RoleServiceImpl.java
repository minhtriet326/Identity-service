package com.devteria.identity_service.services;

import com.devteria.identity_service.dtos.requests.RoleRequest;
import com.devteria.identity_service.dtos.responses.RoleResponse;
import com.devteria.identity_service.entities.Permission;
import com.devteria.identity_service.entities.Role;
import com.devteria.identity_service.mapper.RoleMapper;
import com.devteria.identity_service.repositories.PermissionRepository;
import com.devteria.identity_service.repositories.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;

    RoleMapper roleMapper;

    @Override
    public RoleResponse create(RoleRequest request) {
        Role role = roleMapper.toRole(request);

        Set<Permission> permissions = new HashSet<>(
                permissionRepository.findAllById(request.getPermissions())
        );

        role.setPermissions(permissions);

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleRepository.findAll();

        return roles.stream().map(roleMapper::toRoleResponse).collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        roleRepository.deleteById(id);
    }
}
