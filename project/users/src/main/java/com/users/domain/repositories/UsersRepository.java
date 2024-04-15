package com.users.domain.repositories;

import com.users.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UsersRepository {
    Optional<User> findById(String id);
    Optional<User> findByCpfValue(String cpfValue);
    Optional<User> findByEmailValue(String emailValue);
    User save(User user);
    User update(User user);
    Page<User> findByName(String name, Pageable pageable);
    Page<User> findAll(Pageable pageable);
}
