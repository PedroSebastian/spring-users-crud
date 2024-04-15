package com.users.adapters.input;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.users.adapters.input.handlers.UserPatchHandler;
import com.users.adapters.input.responses.UserResponse;
import com.users.adapters.input.resquests.create.CreateUserRequest;
import com.users.adapters.input.resquests.edit.UpdateUserRequest;
import com.users.application.UsersService;
import com.users.domain.model.User;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Iterator;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController implements IUsersController {
    private final UsersService usersService;
    private final UserPatchHandler userPartialUpdateHandler;

    public UsersController(UsersService usersService, UserPatchHandler userPartialUpdateHandler) {
        this.usersService = usersService;
        this.userPartialUpdateHandler = userPartialUpdateHandler;
    }

    @PostMapping
    public ResponseEntity<EntityModel<UserResponse>> registerUser(@RequestBody @Valid CreateUserRequest user) {
        User savedUser = usersService.register(CreateUserRequest.toModel(user));
        UserResponse response = UserResponse.fromModel(savedUser);
        return ResponseEntity.ok(EntityModel.of(response,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsersController.class).getUser(response.getId())).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponse>> getUser(@PathVariable String id) {
        User user = usersService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(EntityModel.of(UserResponse.fromModel(user),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsersController.class).getUser(id)).withSelfRel()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponse>> updateUser(@PathVariable String id, @RequestBody @Valid UpdateUserRequest request) {
        User user = usersService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        User updatedUser = usersService.update(UpdateUserRequest.toModel(user, request));
        return ResponseEntity.ok(EntityModel.of(UserResponse.fromModel(updatedUser),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsersController.class).getUser(id)).withSelfRel()));
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<?> patchUser(@PathVariable String id, @RequestBody JsonPatch patch) {
        try {
            User user = usersService.findById(id);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            var objectMapper = new ObjectMapper();
            JsonNode patchNode = objectMapper.convertValue(patch, JsonNode.class);
            Iterator<JsonNode> elements = patchNode.elements();

            while (elements.hasNext()) {
                JsonNode operation = elements.next();
                String opType = operation.get("op").textValue();
                if (!opType.equals("replace") && !opType.equals("add")) {
                    return ResponseEntity.badRequest().body("Unsupported JSON Patch operation: " + opType);
                }
            }

            User updatedUser = userPartialUpdateHandler.applyPatchToUser(patch, user);
            usersService.update(updatedUser);
            return ResponseEntity.ok(EntityModel.of(UserResponse.fromModel(updatedUser),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsersController.class).getUser(id)).withSelfRel()));

        } catch (JsonPatchException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping
    public ResponseEntity<Page<EntityModel<UserResponse>>> listUsers(@RequestParam(required = false) String name, Pageable pageable) {
        Page<User> users = (name == null) ?
                usersService.findAll(pageable) :
                usersService.findByName(name, pageable);

        Page<EntityModel<UserResponse>> modelPage = users.map(user -> {
            UserResponse response = UserResponse.fromModel(user);
            return EntityModel.of(response,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsersController.class).getUser(user.getId())).withSelfRel());
        });

        return ResponseEntity.ok(modelPage);
    }
}
