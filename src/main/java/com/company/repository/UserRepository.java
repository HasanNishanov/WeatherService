package com.company.repository;


import com.company.model.dto.UserDTO;
import com.company.model.entity.UserEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@Repository

public interface UserRepository extends R2dbcRepository<UserEntity, Long> {
    Mono<UserEntity> findByUsername(String username);

    @Modifying
    @Query("UPDATE users SET subscription = cityName  WHERE id = userId")
    Mono<Integer> updateCitySubscription(Long userId, String cityName);


}
