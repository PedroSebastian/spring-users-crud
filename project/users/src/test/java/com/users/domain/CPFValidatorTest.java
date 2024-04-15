package com.users.domain;

import com.users.domain.model.CPFValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CPFValidatorTest {

    @Test
    void whenCPFIsValid_thenShouldReturnTrue() {
        assertTrue(CPFValidator.isValidCPF("123.456.789-09"));
    }

    @Test
    void whenCPFHasAllSameDigits_thenShouldReturnFalse() {
        assertFalse(CPFValidator.isValidCPF("111.111.111-11"));
    }

    @Test
    void whenCPFLessOrMoreThan11Digits_thenShouldReturnFalse() {
        assertFalse(CPFValidator.isValidCPF("123.456.789-0"));
        assertFalse(CPFValidator.isValidCPF("123.456.789-012"));
    }

    @Test
    void whenCPFHasIncorrectCheckDigits_thenShouldReturnFalse() {
        assertFalse(CPFValidator.isValidCPF("123.456.789-00"));
    }

    @Test
    void whenCPFContainsNonNumericCharacters_thenShouldStillValidate() {
        assertTrue(CPFValidator.isValidCPF("935.411.347-80"));
    }

    @Test
    void whenCPFContainsLetters_thenShouldReturnFalse() {
        assertFalse(CPFValidator.isValidCPF("935a411b347-80"));
    }
}
