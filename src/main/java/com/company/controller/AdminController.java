package com.company.controller;

import com.company.model.dto.CityDTO;
import com.company.model.dto.UserDTO;
import com.company.model.entity.CityEntity;
import com.company.model.entity.SubscriptionEntity;
import com.company.model.entity.UserEntity;
import com.company.repository.CityRepository;
import com.company.service.AdminService;
import com.company.service.CityService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;


@RestController
@RequestMapping("/admin/api/v1")
public class AdminController {
    private final AdminService adminService;
    private final CityService cityService;

    public AdminController(AdminService adminService, CityRepository cityRepository, CityService cityService) {
        this.adminService = adminService;
        this.cityService = cityService;
    }


    @GetMapping("/user-list")
    public Flux<UserEntity> getUserList(@RequestHeader("Authorization") String token) {
        adminService.verifyToken(token);
        return adminService.getUserList();
    }

    @GetMapping("/city-list")
    public Flux<CityEntity> getCitiesList() {
        return cityService.getAllCities();
    }

    @PutMapping("/update-city/{id}")
    public Mono<CityEntity> updateCity(@PathVariable Long id, @RequestBody CityDTO cityDTO) {
        return cityService.updateCity(id, cityDTO);
    }


    @GetMapping("/user-details/{userId}")
    public Mono<Tuple2<SubscriptionEntity, UserEntity>> getSubscriptionAndUser(@PathVariable Long userId) {
        return adminService.getSubscriptionAndUser(userId);
    }


    @PutMapping("/edit-user/{userId}")
    public Mono<Object> updateUser(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        return adminService.updateUser(userId, userDTO);
    }
}
