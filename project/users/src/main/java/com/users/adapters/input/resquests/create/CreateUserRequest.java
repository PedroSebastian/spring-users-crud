package com.users.adapters.input.resquests.create;

import com.users.domain.model.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
public class CreateUserRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Password is required")

    @Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters")
    private String password;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "CPF is required")
    private String cpf;

    @Past(message = "Birth date must be in the past")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

    private PhoneRequest phone;
    private AddressRequest address;
    private ProfileRequest profile;

    public static User toModel(CreateUserRequest request) {
        CPF cpfObject = new CPF(request.getCpf());
        Email emailObject = new Email(request.getEmail());
        Phone phoneObject = new Phone(request.getPhone().getNumber(), request.getPhone().getAreaCode(), request.getPhone().getCountryCode());
        Address addressObject = new Address(
            request.getAddress().getStreet(),
            request.getAddress().getNumber(),
            request.getAddress().getCity(),
            request.getAddress().getState(),
            request.getAddress().getCountry(),
            request.getAddress().getZipCode()
        );
        Profile profileObject = new Profile(
            request.getProfile().getLanguagePreference(),
            request.getProfile().getThemePreference(),
            request.getProfile().isReceiveNewsletter(),
            request.getProfile().getTimeZone(),
            request.getProfile().getBio()
        );

        return new User(
            request.getBirthDate(),
            cpfObject,
            emailObject,
            phoneObject,
            addressObject,
            profileObject,
            request.getPassword(),
            request.getName()
        );
    }

    public static CreateUserRequest sampleUserRequest() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Sebastian");
        request.setPassword("my_password_123!!_90!");
        request.setEmail("sebastian@email.com");
        request.setCpf("780.612.220-60");
        request.setBirthDate(LocalDate.of(1990, 7, 27));

        PhoneRequest phone = new PhoneRequest();
        phone.setNumber("998877665");
        phone.setAreaCode("48");
        phone.setCountryCode("55");
        request.setPhone(phone);

        AddressRequest address = new AddressRequest();
        address.setStreet("Rua das Camélias");
        address.setNumber("42");
        address.setCity("Florianópolis");
        address.setState("SC");
        address.setCountry("Brasil");
        address.setZipCode("88000000");
        request.setAddress(address);

        ProfileRequest profile = new ProfileRequest();
        profile.setLanguagePreference("Português");
        profile.setThemePreference("Claro");
        profile.setReceiveNewsletter(true);
        profile.setTimeZone("America/Sao_Paulo");
        profile.setBio("Desenvolvedor apaixonado por tecnologia e inovação.");
        request.setProfile(profile);

        return request;
    }
}

