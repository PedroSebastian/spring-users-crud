package com.users.testdata;

import com.users.adapters.input.resquests.create.AddressRequest;
import com.users.adapters.input.resquests.create.CreateUserRequest;
import com.users.adapters.input.resquests.create.PhoneRequest;
import com.users.adapters.input.resquests.create.ProfileRequest;
import com.users.domain.model.*;

import java.time.LocalDate;

public class SampleUser {

    public static User createSampleUser() {
        CPF cpf = new CPF("12345678909");
        Email email = new Email("sebastian@email.com");
        Phone phone = new Phone("997318843", "048", "55");
        Address address = new Address(
                "Rua das Camélias", "2155", "Florianópolis", "SC", "Brasil", "88034101"
        );

        Profile profile = new Profile(
                "Português", "Claro", true, "America/Sao_Paulo", "Desenvolvedor apaixonado por tecnologia e inovação."
        );

        return new User(
                LocalDate.of(1990, 1, 1),
                cpf,
                email,
                phone,
                address,
                profile,
                "my_password_123!!_90!",
                "Sebastian"
        );
    }

    public static CreateUserRequest createSampleUserRequest() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Sebastian");
        request.setPassword("my_password_123!!_90!");
        request.setEmail("sebastian@email.com");
        request.setCpf("12345678909");
        request.setBirthDate(LocalDate.of(1990, 1, 1));  // Data de nascimento

        PhoneRequest phone = new PhoneRequest();
        phone.setNumber("997318843");
        phone.setAreaCode("048");
        phone.setCountryCode("55");
        request.setPhone(phone);

        AddressRequest address = new AddressRequest();
        address.setStreet("Rua das Camélias");
        address.setNumber("2155");
        address.setCity("Florianópolis");
        address.setState("SC");
        address.setCountry("Brasil");
        address.setZipCode("88034101");
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
