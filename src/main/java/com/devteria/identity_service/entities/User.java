package com.devteria.identity_service.entities;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
  @Size(min = 3, message = "Username must be at least 3 characters")
  String username;

  String password;

  String firstName;

  String lastName;

  LocalDate dob;

  @ManyToMany Set<Role> roles;

  @OneToOne(mappedBy = "user")
  RefreshToken refreshToken;
}
