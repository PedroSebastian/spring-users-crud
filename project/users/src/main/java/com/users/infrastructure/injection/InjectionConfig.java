package com.users.infrastructure.injection;

import com.users.adapters.output.CouchbaseUsersRepository;
import com.users.adapters.output.UsersRepositoryAdapter;
import com.users.domain.repositories.UsersRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InjectionConfig {
    @Bean
    public UsersRepository usersRepository(CouchbaseUsersRepository repository) {
        return new UsersRepositoryAdapter(repository);
    }
}
