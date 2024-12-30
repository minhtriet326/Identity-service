package com.devteria.identity_service.entities;

import java.util.Date;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshToken {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  String refreshToken;

  Date expiryTime;

  @OneToOne
  @JoinColumn(name = "user_id")
  User user;
}
