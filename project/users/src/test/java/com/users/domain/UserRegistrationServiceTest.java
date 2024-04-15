package com.users.domain;

import com.users.domain.exceptions.CpfAlreadyRegisteredException;
import com.users.domain.exceptions.EmailAlreadyRegisteredException;
import com.users.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserRegistrationServiceTest {
    private UserRegistrationService service;
    private UsersRepositoryStub repository;

    @BeforeEach
    void setUp() {
        repository = new UsersRepositoryStub();
        service = new UserRegistrationService(repository);
    }

    private User createValidUser() {
        return new User(
                LocalDate.now().minusYears(20), // valid age
                new CPF("387.542.910-97"),
                new Email("user@example.com"),
                new Phone("1234567890", "021", "55"),
                new Address("Street", "1000", "City", "State", "Country", "12345"),
                new Profile("English", "Light", true, "GMT", "No bio"),
                "password123",
                "John Doe"
        );
    }

    @Test
    void whenRegisteringUserWithUniqueCpfAndEmail_thenSucceed() {
        User user = createValidUser();
        assertDoesNotThrow(() -> service.registerUser(user));
    }

    @Test
    void whenRegisteringUserWithDuplicateCpf_thenThrowCpfAlreadyRegisteredException() {
        User firstUser = createValidUser();
        repository.save(firstUser);

        User secondUser = createValidUser();
        secondUser.getCpf().setValue("387.542.910-97");
        assertThrows(CpfAlreadyRegisteredException.class, () -> service.registerUser(secondUser));
    }

    @Test
    void whenRegisteringUserWithDuplicateEmail_thenThrowEmailAlreadyRegisteredException() throws NoSuchFieldException, IllegalAccessException {
        User firstUser = createValidUser();
        repository.save(firstUser);

        User secondUser = createValidUser();
        secondUser.getEmail().setValue("user@example.com");

        Field cpfField = secondUser.getClass().getDeclaredField("cpf");
        cpfField.setAccessible(true);
        cpfField.set(secondUser, new CPF("401.035.550-64"));

        assertThrows(EmailAlreadyRegisteredException.class, () -> service.registerUser(secondUser));
    }
}
