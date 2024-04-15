package com.users.domain.model;

import com.users.domain.exceptions.InvalidCPFException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CPF {
    private String value;

    public CPF(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("CPF is required.");
        }
        if (!isValidCPF(value)) {
            throw new InvalidCPFException("Invalid CPF.");
        }
        this.value = value;
    }

    public void setValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("CPF is required.");
        }
        if (!isValidCPF(value)) {
            throw new InvalidCPFException("Invalid CPF.");
        }
        this.value = value;
    }

    private boolean isValidCPF(String cpf) {
        return CPFValidator.isValidCPF(cpf);
    }
}
