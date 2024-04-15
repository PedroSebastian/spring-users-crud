package com.users.domain;

import com.users.domain.model.User;
import com.users.domain.repositories.UsersRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class UsersRepositoryStub implements UsersRepository {
    private final Map<String, User> users = new HashMap<>();

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByCpfValue(String cpfValue) {
        return users.values().stream()
                .filter(u -> u.getCpf().getValue().equals(cpfValue))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmailValue(String emailValue) {
        return users.values().stream()
                .filter(u -> u.getEmail().getValue().equals(emailValue))
                .findFirst();
    }

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Page<User> findByName(String name, Pageable pageable) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
