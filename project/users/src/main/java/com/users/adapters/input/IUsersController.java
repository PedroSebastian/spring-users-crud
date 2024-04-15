package com.users.adapters.input;

import com.github.fge.jsonpatch.JsonPatch;
import com.users.adapters.input.responses.UserResponse;
import com.users.adapters.input.resquests.create.CreateUserRequest;
import com.users.adapters.input.resquests.edit.UpdateUserRequest;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Users", description = "REST API for user operations.")
public interface IUsersController {

    @Operation(summary = "Register a new user", description = "Creates a new user in the system and returns the created user with a HATEOAS link.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user data provided")
    })
    ResponseEntity<EntityModel<UserResponse>> registerUser(@RequestBody @Valid CreateUserRequest user);

    @Operation(summary = "Get a user by ID", description = "Retrieves a user by their unique identifier with a HATEOAS link.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    ResponseEntity<EntityModel<UserResponse>> getUser(@PathVariable String id);

    @Operation(summary = "Update a user", description = "Updates a user's information based on their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user data provided"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    ResponseEntity<EntityModel<UserResponse>> updateUser(@PathVariable String id, @RequestBody @Valid UpdateUserRequest request);

    @Operation(summary = "Apply JSON patch to a user", description = "Applies JSON Patch to partially update a user's information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User patched successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid JSON Patch provided"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    ResponseEntity<?> patchUser(@PathVariable String id, @RequestBody JsonPatch patch);

    @Operation(summary = "List users", description = "Lists users in the system, optionally filtered by name. Returns a page of users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users listed successfully",
                    content = @Content(schema = @Schema(implementation = PagedModel.class)))
    })
    ResponseEntity<Page<EntityModel<UserResponse>>> listUsers(@RequestParam(required = false) String name, Pageable pageable);
}

