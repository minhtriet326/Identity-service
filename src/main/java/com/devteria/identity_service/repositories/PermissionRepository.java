package com.devteria.identity_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devteria.identity_service.entities.Permission;

public interface PermissionRepository extends JpaRepository<Permission, String> {}
