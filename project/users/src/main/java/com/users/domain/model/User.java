    package com.users.domain.model;

    import com.users.domain.exceptions.UnderageException;
    import lombok.Getter;
    import org.springframework.data.annotation.Id;
    import org.springframework.data.couchbase.core.mapping.Document;
    import org.springframework.data.couchbase.core.mapping.Field;

    import java.time.LocalDate;
    import java.time.Period;
    import java.util.UUID;

    @Getter
    @Document
    public class User {
        @Id
        @Field
        private String id = UUID.randomUUID().toString();
        private LocalDate createdAt;
        private LocalDate editedAt;
        private LocalDate birthDate;
        private final CPF cpf;
        private final Email email;
        private String name;
        private String password;
        private Phone phone;
        private Address address;
        private Profile profile;
        private boolean active = true;

        public User(LocalDate birthDate, CPF cpf, Email email, Phone phone, Address address, Profile profile, String password, String name) {
            if (birthDate == null || cpf == null || email == null || address == null || (password == null || password.isBlank()) || (name == null || name.isBlank())) {
                throw new IllegalArgumentException("Email, CPF, address, password, and name are required.");
            }
            if (this.getCreatedAt() == null) {
                this.createdAt = LocalDate.now();
            }
            this.birthDate = birthDate;
            this.cpf = cpf;
            this.email = email;
            this.phone = phone;
            this.address = address;
            this.profile = profile;
            this.password = password;
            this.name = name;
            validateAge();
        }

        private void validateAge() {
            if (Period.between(birthDate, LocalDate.now()).getYears() < 18) {
                throw new UnderageException("User must be over 18 years old.");
            }
        }

        public void updateProfile(Profile profile) {
            this.profile = profile;
            this.editedAt = LocalDate.now();
        }

        public void disableUser() {
            this.active = false;
            this.editedAt = LocalDate.now();
        }

        public void enableUser() {
            this.active = true;
            this.editedAt = LocalDate.now();
        }

        public void updateAddress(Address address) {
            this.address = address;
            this.editedAt = LocalDate.now();
        }

        public void updatePhone(Phone phone) {
            this.phone = phone;
            this.editedAt = LocalDate.now();
        }

        public void changePassword(String password) {
            this.password = password;
            this.editedAt = LocalDate.now();
        }

        public void updateName(String name) {
            this.name = name;
            this.editedAt = LocalDate.now();
        }

        public void updateBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            this.editedAt = LocalDate.now();
            validateAge();
        }

        public String getPassword() {
            return "**********";
        }
    }
