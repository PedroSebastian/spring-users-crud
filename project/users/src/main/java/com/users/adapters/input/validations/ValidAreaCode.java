package com.users.adapters.input.validations;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

@Documented
@Constraint(validatedBy = {})  // Nenhuma implementação de validador é necessária para expressões regulares simples
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp = "\\d{2,5}", message = "Area code must be between 2 and 5 digits")
public @interface ValidAreaCode {
    String message() default "Invalid area code format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
