package com.devteria.identity_service.validators;

import java.time.LocalDate;
import java.time.Period;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {
//    private int minAge;
//
//    @Override
//    public void initialize(DobConstraint constraintAnnotation) {
//        minAge = constraintAnnotation.min();
//    }
//
//    @Override
//    public boolean isValid(LocalDate dob, ConstraintValidatorContext constraintValidatorContext) {
//        if (dob == null) return true; // chỉ kiểm tra dob >= min, ko ktra null
//
//        LocalDate currentDate = LocalDate.now();
//        Period age = Period.between(dob, currentDate);
//
//        return age.getYears() >= minAge;
//    }
// }

public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {
  private int minAge;

  @Override
  public void initialize(DobConstraint constraintAnnotation) {
    minAge = constraintAnnotation.min();
  }

  @Override
  public boolean isValid(LocalDate dob, ConstraintValidatorContext constraintValidatorContext) {
    if (dob == null) return true;

    Period age = Period.between(dob, LocalDate.now());
    return age.getYears() >= minAge;
  }
}
