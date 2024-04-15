package com.users.adapters.input.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.users.adapters.input.resquests.edit.UpdateUserRequest;
import jakarta.validation.*;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.users.domain.model.User;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class UserPatchHandler {
    private final ObjectMapper objectMapper;
    private final Validator validator;

    private static final Set<String> unmodifiableFields = Set.of("id", "cpf", "createdAt", "editedAt", "email");

    public UserPatchHandler(ObjectMapper objectMapper, ValidatorFactory validatorFactory) {
        this.objectMapper = objectMapper;
        this.validator = validatorFactory.getValidator();
    }

    public User applyPatchToUser(JsonPatch patch, User targetUser) throws JsonPatchException, IOException {
        validateImmutableFields(objectMapper.valueToTree(patch));

        JsonNode targetUserNode = objectMapper.valueToTree(targetUser);
        JsonNode patchedUserNode = patch.apply(targetUserNode);
        User patchedUser = objectMapper.treeToValue(patchedUserNode, User.class);

        UpdateUserRequest patchedUserDTO = UpdateUserRequest.fromModel(patchedUser);
        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(patchedUserDTO);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return objectMapper.treeToValue(patchedUserNode, User.class);
    }

    private void validateImmutableFields(JsonNode patchOperations) {
        for (JsonNode operation : patchOperations) {
            String path = operation.get("path").asText();
            if (unmodifiableFields.contains(path.substring(1))) { // Remove the leading '/' from path
                throw new IllegalArgumentException("Field " + path.substring(1) + " cannot be modified.");
            }
        }
    }
}
