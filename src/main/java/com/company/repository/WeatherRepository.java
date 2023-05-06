package com.company.repository;

import com.company.model.entity.WeatherEntity;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends R2dbcRepository<WeatherEntity, String> {
}
