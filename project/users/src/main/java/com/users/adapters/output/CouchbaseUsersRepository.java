package com.users.adapters.output;

import com.users.domain.model.User;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouchbaseUsersRepository extends CouchbaseRepository<User, String> {
    @Query("#{#n1ql.selectEntity} WHERE `cpf`.`value` = $1")
    Optional<User> findByCpfValue(String cpfValue);

    @Query("#{#n1ql.selectEntity} WHERE `email`.`value` = $1")
    Optional<User> findByEmailValue(String emailValue);

    @Query("#{#n1ql.selectEntity} WHERE LOWER(`name`) LIKE '%' || LOWER($1) || '%'")
    Page<User> findByName(String name, Pageable pageable);

    Page<User> findAll(Pageable pageable);
}

