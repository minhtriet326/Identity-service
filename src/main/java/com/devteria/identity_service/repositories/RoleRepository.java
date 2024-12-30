package com.devteria.identity_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devteria.identity_service.entities.Role;

public interface RoleRepository extends JpaRepository<Role, String> {}
