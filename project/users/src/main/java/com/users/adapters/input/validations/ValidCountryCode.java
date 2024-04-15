package com.users.adapters.input.validations;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

@Documented
@Constraint(validatedBy = {})
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp = "\\+\\d{1,4}", message = "Country code must be in the format + followed by 1 to 4 digits")
public @interface ValidCountryCode {
    String message() default "Invalid country code format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
