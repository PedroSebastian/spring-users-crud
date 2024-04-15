package com.users.domain;

import com.users.domain.exceptions.InvalidEmailException;
import com.users.domain.model.Email;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void whenEmailIsValid_thenCreateEmail() {
        assertDoesNotThrow(() -> new Email("example@example.com"));
        assertDoesNotThrow(() -> new Email("info.mail@example.co.uk"));
        assertDoesNotThrow(() -> new Email("user+mailbox/department=shipping@example.com"));
    }

    @Test
    void whenEmailIsInvalid_thenThrowInvalidEmailException() {
        assertThrows(InvalidEmailException.class, () -> new Email("example.com"));
        assertThrows(InvalidEmailException.class, () -> new Email("user@/example.com"));
        assertThrows(InvalidEmailException.class, () -> new Email("user@example"));
    }

    @Test
    void whenEmailIsNull_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Email(null));
    }

    @Test
    void whenEmailIsEmpty_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Email(""));
        assertThrows(IllegalArgumentException.class, () -> new Email("    "));
    }

    @Test
    void whenSettingInvalidEmail_thenThrowInvalidEmailException() {
        Email email = new Email("valid@example.com");
        assertThrows(InvalidEmailException.class, () -> email.setValue("bademail.com"));
    }

    @Test
    void whenEmailIsSetToValid_thenUpdateValue() {
        Email email = new Email("initial@example.com");
        assertDoesNotThrow(() -> email.setValue("new.valid@email.com"));
        assertEquals("new.valid@email.com", email.getValue());
    }
}

