package com.users.integration;

import com.users.application.UsersService;
import com.users.domain.exceptions.CpfAlreadyRegisteredException;
import com.users.domain.exceptions.EmailAlreadyRegisteredException;
import com.users.domain.model.CPF;
import com.users.domain.model.User;
import com.users.testdata.SampleUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.couchbase.BucketDefinition;
import org.testcontainers.couchbase.CouchbaseContainer;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.reflect.Field;
import java.time.Duration;

@SpringBootTest
@Testcontainers
@ExtendWith(SpringExtension.class)
class UsersServiceIntegrationTest {

    @Container
    @ServiceConnection
    static CouchbaseContainer couchbaseContainer = new CouchbaseContainer("couchbase/server:latest")
            .withCredentials("Administrator", "password")
            .withBucket(new BucketDefinition("users-bucket").withPrimaryIndex(true))
            .withStartupTimeout(Duration.ofSeconds(30));

    @DynamicPropertySource
    static void bindCouchbaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.couchbase.connection-string", couchbaseContainer::getConnectionString);
        registry.add("spring.couchbase.username", couchbaseContainer::getUsername);
        registry.add("spring.couchbase.password", couchbaseContainer::getPassword);
        registry.add("spring.data.couchbase.bucket-name", () -> "users-bucket");
    }

    @Autowired
    private CouchbaseTemplate couchbaseTemplate;

    @Autowired
    private UsersService usersService;

    @BeforeEach
    void setUp() {
        couchbaseTemplate.removeByQuery(User.class).all();
    }

    @Test
    void givenValidUser_whenRegister_thenSuccess() {
        User sampleUser = SampleUser.createSampleUser();
        User registeredUser = usersService.register(sampleUser);
        Assertions.assertNotNull(registeredUser);
        Assertions.assertEquals(sampleUser.getEmail().getValue(), registeredUser.getEmail().getValue());
    }

    @Test
    void givenUserWithDuplicateCPF_whenRegister_thenThrowException() {
        User sampleUser = SampleUser.createSampleUser();
        usersService.register(sampleUser); // Register the user first time
        Assertions.assertThrows(CpfAlreadyRegisteredException.class, () -> usersService.register(sampleUser));
    }

    @Test
    void givenUserWithDuplicateEmail_whenRegister_thenThrowException() throws IllegalAccessException, NoSuchFieldException {
        User sampleUser = SampleUser.createSampleUser();
        usersService.register(sampleUser);

        Field cpfField = User.class.getDeclaredField("cpf");
        cpfField.setAccessible(true);
        CPF newCpf = new CPF("439.796.950-77");
        cpfField.set(sampleUser, newCpf);

        Assertions.assertThrows(EmailAlreadyRegisteredException.class, () -> usersService.register(sampleUser));
    }

    @Test
    void givenValidUser_whenUpdateProfile_thenSuccess() {
        User sampleUser = SampleUser.createSampleUser();
        usersService.register(sampleUser); // First register the user
        sampleUser.getProfile().setBio("Updated Bio");
        User updatedUser = usersService.update(sampleUser);
        Assertions.assertEquals("Updated Bio", updatedUser.getProfile().getBio());
    }

    @Test
    void givenUserName_whenFindByName_thenSuccess() {
        User sampleUser = SampleUser.createSampleUser();
        usersService.register(sampleUser); // First register the user
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> result = usersService.findByName(sampleUser.getName(), pageable);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.getContent().size());
        Assertions.assertEquals(sampleUser.getName(), result.getContent().getFirst().getName());
    }
}
