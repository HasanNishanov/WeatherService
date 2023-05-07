package com.company.controller;

import com.company.model.dto.CityDTO;
import com.company.model.entity.CityEntity;
import com.company.service.CityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/cities")
public class CityController {
    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public Flux<CityEntity> getAllCities() {
        return cityService.getAllCities();
    }

    @GetMapping("/{name}")
    public Mono<CityEntity> getCityByName(@PathVariable String name) {
        return cityService.getCityByName(name);
    }

    @PostMapping
    public Mono<CityEntity> createCity(@RequestBody CityEntity city) {
        return cityService.createCity(city);
    }

    @PutMapping("/{id}")
    public Mono<CityEntity> updateCity(@PathVariable Long id, @RequestBody CityDTO city) {
        return cityService.updateCity(id, city);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCity(@PathVariable Long id) {
        return cityService.deleteCity(id);
    }
}