package com.users.adapters.input.responses;

import com.users.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private final String id;
    private final String name;
    private final String email;
    private final String cpf;
    private final String birthDate;
    private final PhoneResponse phone;
    private final AddressResponse address;
    private final ProfileResponse profile;
    private final boolean active;

    public static UserResponse fromModel(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail().getValue(),
                user.getCpf().getValue(),
                user.getBirthDate().toString(),
                PhoneResponse.fromModel(user.getPhone()),
                AddressResponse.fromModel(user.getAddress()),
                ProfileResponse.fromModel(user.getProfile()),
                user.isActive()
        );
    }
}
