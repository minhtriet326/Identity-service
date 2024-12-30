package com.devteria.identity_service.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

// @Target(ElementType.FIELD)
// @Retention(RetentionPolicy.RUNTIME)
// @Constraint(validatedBy = {DobValidator.class})
// public @interface DobConstraint {
//    String message() default "Invalid day of birth";
//
//    Class<?>[] groups() default {};
//
//    Class<? extends Payload>[] payload() default {};
//
//    int min();
// }

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {DobValidator.class})
public @interface DobConstraint {
  String message() default "Invalid day of birth";

  int min();

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
