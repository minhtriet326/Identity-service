package com.devteria.identity_service.entities;

import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
  @Id String name;

  String description;

  @ManyToMany Set<Permission> permissions;
}
