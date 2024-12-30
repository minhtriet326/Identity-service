package com.devteria.identity_service.dtos.requests;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.devteria.identity_service.validators.DobConstraint;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
  @Size(min = 3, max = 10, message = "PASSWORD_INVALID")
  String password;

  @NotBlank(message = "Firstname can't be blank")
  String firstName;

  @NotBlank(message = "Lastname can't be blank")
  String lastName;

  @DobConstraint(min = 18, message = "INVALID_DOB")
  LocalDate dob;

  Set<String> roles;
}
