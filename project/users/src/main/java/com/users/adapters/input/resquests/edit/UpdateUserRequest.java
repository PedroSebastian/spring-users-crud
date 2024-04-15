package com.users.adapters.input.resquests.edit;

import com.users.domain.model.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateUserRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters")
    private String password;

    @Past(message = "Birth date must be in the past")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

    @NotNull(message = "Phone is required")
    private PhoneRequest phone;
    @NotNull(message = "Address is required")
    private AddressRequest address;
    @NotNull(message = "Profile is required")
    private ProfileRequest profile;

    private boolean active;

    public static User toModel(User target, UpdateUserRequest request) {
        if (request.getName() != null && !request.getName().isBlank()) {
            target.updateName(request.getName());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            target.changePassword(request.getPassword());
        }
        if (request.getBirthDate() != null) {
            target.updateBirthDate(request.getBirthDate());
        }
        if (request.getPhone() != null) {
            target.updatePhone(new Phone(request.getPhone().getNumber(), request.getPhone().getAreaCode(), request.getPhone().getCountryCode()));
        }
        if (request.getAddress() != null) {
            target.updateAddress(new Address(
                request.getAddress().getStreet(),
                request.getAddress().getNumber(),
                request.getAddress().getCity(),
                request.getAddress().getState(),
                request.getAddress().getCountry(),
                request.getAddress().getZipCode()
            ));
        }
        if (request.getProfile() != null) {
            target.updateProfile(new Profile(
                request.getProfile().getLanguagePreference(),
                request.getProfile().getThemePreference(),
                request.getProfile().isReceiveNewsletter(),
                request.getProfile().getTimeZone(),
                request.getProfile().getBio()
            ));
        }

        if (request.isActive()) {
            target.enableUser();
        } else {
            target.disableUser();
        }
        return target;
    }

    public static UpdateUserRequest fromModel(User user) {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setName(user.getName());
        request.setPassword(user.getPassword());
        request.setBirthDate(user.getBirthDate());
        request.setPhone(new PhoneRequest(user.getPhone().getNumber(), user.getPhone().getAreaCode(), user.getPhone().getCountryCode()));
        request.setAddress(new AddressRequest(user.getAddress().getStreet(), user.getAddress().getNumber(), user.getAddress().getCity(), user.getAddress().getState(), user.getAddress().getCountry(), user.getAddress().getZipCode()));
        request.setProfile(new ProfileRequest(user.getProfile().getLanguagePreference(), user.getProfile().getThemePreference(), user.getProfile().isReceiveNewsletter(), user.getProfile().getTimeZone(), user.getProfile().getBio()));
        request.setActive(user.isActive());
        return request;
    }
}

