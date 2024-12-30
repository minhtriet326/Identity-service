package com.devteria.identity_service.dtos.requests;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.devteria.identity_service.validators.DobConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
  @Size(min = 3, max = 10, message = "USERNAME_INVALID")
  String username;

  @Size(min = 3, message = "PASSWORD_INVALID")
  String password;

  @NotBlank(message = "Firstname can't be blank")
  String firstName;

  @NotBlank(message = "Lastname can't be blank")
  String lastName;

  @DobConstraint(min = 18, message = "INVALID_DOB")
  LocalDate dob;

  Set<String> roles;
}

// @Data
// @AllArgsConstructor
// @NoArgsConstructor
// @FieldDefaults(level = AccessLevel.PRIVATE)
// public class UserCreationRequest {
//    @Size(min = 3, message = "USERNAME_INVALID")
//    String username;
//
//    @Size(min = 3, max = 10, message = "PASSWORD_INVALID")
//    String password;
//
//    @NotBlank(message = "Firstname can't be blank")
//    String firstName;
//
//    @NotBlank(message = "Lastname can't be blank")
//    String lastName;
//
//    @DobConstraint(min = 18, message = "INVALID_DOB")
//    LocalDate dob;
//
//    Set<String> roles;
// }
