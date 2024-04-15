package com.users.adapters.input.validations;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

@Documented
@Constraint(validatedBy = {}) // Nenhuma implementação de validador necessária para expressões regulares simples
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp = "^[0-9]{5}(-[0-9]{4})?$", message = "Zip code must be in the format 12345 or 12345-6789")
public @interface ValidZipCode {
    String message() default "Invalid zip code format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
