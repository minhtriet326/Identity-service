package com.devteria.identity_service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devteria.identity_service.entities.User;

public interface UserRepository extends JpaRepository<User, String> {
  public Boolean existsByUsername(String username);

  public Optional<User> findByUsername(String username);
}
