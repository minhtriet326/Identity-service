package com.devteria.identity_service.repositories;

import com.devteria.identity_service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    public Boolean existsByUsername(String username);
    public Optional<User> findByUsername(String username);
}
