package com.company.service;

import com.company.model.dto.CityDTO;
import com.company.model.entity.CityEntity;
import com.company.repository.CityRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class CityService {
    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public Flux<CityEntity> getAllCities() {
        return cityRepository.findAllCities();
    }

    public Mono<CityEntity> getCityByName(String name) {
        return cityRepository.getCityByName(name);
    }

    public Mono<CityEntity> createCity(CityEntity city) {
        return cityRepository.save(city);
    }

    public Mono<CityEntity> updateCity(Long id, CityDTO city) {
        return cityRepository.findById(id)
                .flatMap(existingCity -> {
                    existingCity.setName(city.getName());
                    existingCity.setTemperature(city.getTemperature());
                    existingCity.setVisible(city.getVisible());
                    return cityRepository.save(existingCity);
                });
    }

    public Mono<Void> deleteCity(Long id) {
        return cityRepository.deleteById(id);
    }
}