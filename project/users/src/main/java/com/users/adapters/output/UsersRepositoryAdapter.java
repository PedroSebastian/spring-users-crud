package com.users.adapters.output;

import com.users.domain.model.User;
import com.users.domain.repositories.UsersRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UsersRepositoryAdapter implements UsersRepository {
    private final CouchbaseUsersRepository couchbaseUsersRepository;

    public UsersRepositoryAdapter(CouchbaseUsersRepository couchbaseUsersRepository) {
        this.couchbaseUsersRepository = couchbaseUsersRepository;
    }

    @Override
    public Optional<User> findById(String id) {
        return couchbaseUsersRepository.findById(id);
    }

    @Override
    public Optional<User> findByCpfValue(String cpfValue) {
        return couchbaseUsersRepository.findByCpfValue(cpfValue);
    }

    @Override
    public Optional<User> findByEmailValue(String emailValue) {
        return couchbaseUsersRepository.findByEmailValue(emailValue);
    }

    @Override
    public User save(User user) {
        return couchbaseUsersRepository.save(user);
    }

    @Override
    public User update(User user) {
        return couchbaseUsersRepository.save(user);
    }

    @Override
    public Page<User> findByName(String name, Pageable pageable) {
        return couchbaseUsersRepository.findByName(name, pageable);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return couchbaseUsersRepository.findAll(pageable);
    }
}
