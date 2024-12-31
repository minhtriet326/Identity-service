package com.devteria.identity_service.repositories;

import com.devteria.identity_service.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, String> {
}
