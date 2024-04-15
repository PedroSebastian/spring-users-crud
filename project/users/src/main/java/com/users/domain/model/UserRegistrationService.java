package com.users.domain.model;

import com.users.domain.exceptions.CpfAlreadyRegisteredException;
import com.users.domain.exceptions.EmailAlreadyRegisteredException;
import com.users.domain.repositories.UsersRepository;

public class UserRegistrationService {
    private final UsersRepository usersRepository;

    public UserRegistrationService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public User registerUser(User user) {
        checkIfCpfIsAlreadyRegistered(user.getCpf());
        checkIfEmailIsAlreadyRegistered(user.getEmail());
        return usersRepository.save(user);
    }

    private void checkIfCpfIsAlreadyRegistered(CPF cpf) {
        usersRepository.findByCpfValue(cpf.getValue()).ifPresent(user -> {
            throw new CpfAlreadyRegisteredException("CPF already registered.");
        });
    }

    private void checkIfEmailIsAlreadyRegistered(Email email) {
        usersRepository.findByEmailValue(email.getValue()).ifPresent(user -> {
            throw new EmailAlreadyRegisteredException("Email already registered.");
        });
    }
}
