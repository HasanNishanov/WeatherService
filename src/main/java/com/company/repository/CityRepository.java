package com.company.repository;

import com.company.model.dto.CityDTO;
import com.company.model.entity.CityEntity;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@EnableR2dbcRepositories
public interface CityRepository extends R2dbcRepository<CityEntity, Long> {
    Mono<CityEntity> getCityByName(String name);

    @Query("SELECT name,temperature FROM city")
    Flux<CityEntity> findAllCities();

    @Query("SELECT name FROM city ")
    Mono<CityEntity> findByName(String name);
}
