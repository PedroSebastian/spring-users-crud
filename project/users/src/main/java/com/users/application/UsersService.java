package com.users.application;

import com.users.domain.model.User;
import com.users.domain.model.UserRegistrationService;
import com.users.domain.repositories.UsersRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public User findById(String id) {
        return usersRepository.findById(id).orElse(null);
    }

    public User register(User user) {
        var registrationService = new UserRegistrationService(usersRepository);
        return registrationService.registerUser(user);
    }

    public User update(User user) {
        return usersRepository.save(user);
    }

    public Page<User> findByName(String name, Pageable pageable) {
        return usersRepository.findByName(name, pageable);
    }

    public Page<User> findAll(Pageable pageable) {
        return usersRepository.findAll(pageable);
    }
}
