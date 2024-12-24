package com.devteria.identity_service.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

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

    @Column(unique = true)
    @Size(min = 3, message = "Username must be at least 3 characters")
    String username;

    String password;

    String firstName;

    String lastName;

    LocalDate dob;

    @ManyToMany
    Set<Role> roles;

    @OneToOne(mappedBy = "user")
    RefreshToken refreshToken;
}
