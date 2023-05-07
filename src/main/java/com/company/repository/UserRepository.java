package com.company.repository;


import com.company.model.dto.UserDTO;
import com.company.model.entity.UserEntity;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<UserEntity, String> {
    Mono<UserEntity> findByUsername(String username);
}
