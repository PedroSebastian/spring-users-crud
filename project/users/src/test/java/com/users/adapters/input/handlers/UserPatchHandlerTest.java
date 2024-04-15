package com.users.adapters.input.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.users.testdata.SampleUser;
import com.users.domain.model.User;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserPatchHandlerTest {

    @Autowired
    private ObjectMapper objectMapper;

    private UserPatchHandler userPatchHandler;

    @BeforeEach
    void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        userPatchHandler = new UserPatchHandler(objectMapper, validatorFactory);
    }

    private User createSampleUser() {
        return SampleUser.createSampleUser();
    }

    @Test
    void givenValidPatch_whenApplyPatchToUser_thenSucceed() throws JsonPatchException, IOException {
        User user = createSampleUser();
        String patchString = "[{\"op\": \"replace\", \"path\": \"/name\", \"value\": \"Jane Doe\"}]";
        JsonPatch patch = JsonPatch.fromJson(this.objectMapper.readTree(patchString));

        User updatedUser = userPatchHandler.applyPatchToUser(patch, user);

        assertEquals("Jane Doe", updatedUser.getName());
    }

    @Test
    void givenPatchModifyingImmutableField_whenApplyPatchToUser_thenThrowException() throws IOException {
        User user = createSampleUser();
        String patchString = "[{\"op\": \"replace\", \"path\": \"/id\", \"value\": \"newId\"}]";
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchString));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userPatchHandler.applyPatchToUser(patch, user);
        });

        assertTrue(exception.getMessage().contains("Field id cannot be modified"));
    }
}

