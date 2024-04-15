package com.users.adapters.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.users.adapters.input.handlers.UserPatchHandler;
import com.users.testdata.SampleUser;
import com.users.application.UsersService;
import com.users.domain.model.User;
import com.users.infrastructure.exceptionhandler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UsersController.class, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GlobalExceptionHandler.class)
})
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersService usersService;

    @MockBean
    private UserPatchHandler userPatchHandler;

    @Autowired
    private ObjectMapper objectMapper;

    private User sampleUser;

    @BeforeEach
    public void setup() {
        sampleUser = SampleUser.createSampleUser();
    }

    @Test
    void givenValidUserId_whenGetUser_thenReturnUser() throws Exception {
        when(usersService.findById("1")).thenReturn(sampleUser);

        mockMvc.perform(get("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sebastian"));
    }

    @Test
    void givenInvalidUserId_whenGetUser_thenNotFound() throws Exception {
        when(usersService.findById("2")).thenReturn(null);

        mockMvc.perform(get("/api/v1/users/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenValidUser_whenRegisterUser_thenCreateUser() throws Exception {
        when(usersService.register(any(User.class))).thenReturn(sampleUser);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(SampleUser.createSampleUserRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sebastian"));
    }

    @Test
    void givenValidUserIdAndPatch_whenPatchUser_thenUpdateUser() throws Exception {
        var updatedUser = sampleUser;
        updatedUser.updateName("Jane Doe");

        when(usersService.findById("1")).thenReturn(sampleUser);
        when(userPatchHandler.applyPatchToUser(any(), any())).thenReturn(updatedUser);

        mockMvc.perform(patch("/api/v1/users/1")
                        .contentType("application/json-patch+json")
                        .content("[{\"op\": \"replace\", \"path\": \"/name\", \"value\": \"Jane Doe\"}]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Doe"));
    }
}

