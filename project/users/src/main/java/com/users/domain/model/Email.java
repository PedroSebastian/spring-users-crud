package com.users.domain.model;

import com.users.domain.exceptions.InvalidEmailException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Getter
@NoArgsConstructor
public class Email {
    private String value;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)+$"
    );

    public Email(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email is required.");
        }
        if (!isValidEmail(value)) {
            throw new InvalidEmailException("Invalid Email.");
        }
        this.value = value.toLowerCase();
    }

    public void setValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email is required.");
        }
        if (!isValidEmail(value)) {
            throw new InvalidEmailException("Invalid Email.");
        }
        this.value = value.toLowerCase();
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
