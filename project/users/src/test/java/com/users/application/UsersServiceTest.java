package com.users.application;

import com.users.domain.exceptions.CpfAlreadyRegisteredException;
import com.users.domain.exceptions.EmailAlreadyRegisteredException;
import com.users.domain.model.User;
import com.users.domain.repositories.UsersRepository;
import com.users.testdata.SampleUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UsersService usersService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = SampleUser.createSampleUser();
    }

    @Test
    void givenValidUser_whenRegister_thenSuccess() {
        when(usersRepository.findByCpfValue(any(String.class))).thenReturn(Optional.empty());
        when(usersRepository.findByEmailValue(any(String.class))).thenReturn(Optional.empty());
        when(usersRepository.save(any(User.class))).thenReturn(sampleUser);

        User registeredUser = usersService.register(sampleUser);

        assertNotNull(registeredUser);
        verify(usersRepository).save(sampleUser);
    }

    @Test
    void givenExistingCpf_whenRegister_thenThrowCpfAlreadyRegisteredException() {
        when(usersRepository.findByCpfValue(any(String.class))).thenReturn(Optional.of(sampleUser));

        Exception exception = assertThrows(CpfAlreadyRegisteredException.class, () -> {
            usersService.register(sampleUser);
        });

        assertEquals("CPF already registered.", exception.getMessage());
    }

    @Test
    void givenExistingEmail_whenRegister_thenThrowEmailAlreadyRegisteredException() {
        when(usersRepository.findByEmailValue(any(String.class))).thenReturn(Optional.of(sampleUser));
        when(usersRepository.findByCpfValue(any(String.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(EmailAlreadyRegisteredException.class, () -> {
            usersService.register(sampleUser);
        });

        assertEquals("Email already registered.", exception.getMessage());
    }
}
