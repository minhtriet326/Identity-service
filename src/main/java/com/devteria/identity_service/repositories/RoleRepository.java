package com.devteria.identity_service.repositories;

import com.devteria.identity_service.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
