package com.users.adapters.output;

import com.users.testdata.SampleUser;
import com.users.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersRepositoryAdapterTest {

    @Mock
    private CouchbaseUsersRepository couchbaseUsersRepository;

    @InjectMocks
    private UsersRepositoryAdapter usersRepositoryAdapter;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = SampleUser.createSampleUser();
    }

    @Test
    void findById() {
        when(couchbaseUsersRepository.findById("1")).thenReturn(Optional.of(sampleUser));
        Optional<User> found = usersRepositoryAdapter.findById("1");
        verify(couchbaseUsersRepository, times(1)).findById("1");
        assertEquals(sampleUser, found.orElse(null));
    }

    @Test
    void findByName() {
        Page<User> page = new PageImpl<>(Collections.singletonList(sampleUser));
        when(couchbaseUsersRepository.findByName("Test", PageRequest.of(0, 10))).thenReturn(page);

        Page<User> result = usersRepositoryAdapter.findByName("Test", PageRequest.of(0, 10));
        verify(couchbaseUsersRepository, times(1)).findByName("Test", PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
        assertEquals(sampleUser, result.getContent().getFirst());
    }

    @Test
    void findByCpfValue() {
        when(couchbaseUsersRepository.findByCpfValue("12345678900")).thenReturn(Optional.of(sampleUser));
        Optional<User> found = usersRepositoryAdapter.findByCpfValue("12345678900");
        verify(couchbaseUsersRepository, times(1)).findByCpfValue("12345678900");
        assertTrue(found.isPresent());
        assertEquals(sampleUser, found.get());
    }

    @Test
    void findByEmailValue() {
        when(couchbaseUsersRepository.findByEmailValue("test@example.com")).thenReturn(Optional.of(sampleUser));
        Optional<User> found = usersRepositoryAdapter.findByEmailValue("test@example.com");
        verify(couchbaseUsersRepository, times(1)).findByEmailValue("test@example.com");
        assertTrue(found.isPresent());
        assertEquals(sampleUser, found.get());
    }

    @Test
    void saveUser() {
        when(couchbaseUsersRepository.save(sampleUser)).thenReturn(sampleUser);
        User savedUser = usersRepositoryAdapter.save(sampleUser);
        verify(couchbaseUsersRepository, times(1)).save(sampleUser);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
    }

    @Test
    void updateUser() {
        when(couchbaseUsersRepository.save(sampleUser)).thenReturn(sampleUser);
        User updatedUser = usersRepositoryAdapter.update(sampleUser);
        verify(couchbaseUsersRepository, times(1)).save(sampleUser);
        assertNotNull(updatedUser);
        assertNotNull(updatedUser.getId());
    }

    @Test
    void findAllUsers() {
        Page<User> page = new PageImpl<>(Collections.singletonList(sampleUser));
        when(couchbaseUsersRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);
        Page<User> result = usersRepositoryAdapter.findAll(PageRequest.of(0, 10));
        verify(couchbaseUsersRepository, times(1)).findAll(PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
        assertEquals(sampleUser, result.getContent().getFirst());
    }
}
