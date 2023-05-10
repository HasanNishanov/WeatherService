package com.company.controller;

import com.company.model.dto.UserDTO;
import com.company.model.entity.CityEntity;
import com.company.model.entity.SubscriptionEntity;
import com.company.model.entity.UserEntity;
import com.company.service.CityService;
import com.company.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Map;

@Controller
    @RequestMapping("/user")
public class UserController {

    private ModelMapper modelMapper;

    private final CityService cityService;
    private final UserService userService;

    public UserController(CityService cityService, UserService userService) {
        this.cityService = cityService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<?>> register(@RequestBody Mono<UserDTO> userDTOMono) {
        return userDTOMono
                .map(userDTO -> modelMapper.map(userDTO, UserEntity.class))
                .flatMap(userEntity -> userService.createUser(userEntity)
                        .map(jwtToken -> ResponseEntity.ok(Map.of("jwt", jwtToken))));
    }

    @PostMapping("/subscribe-to-city")
    public Mono<Void> subscribeToCity(@RequestParam Long userId, @RequestParam String cityName, ServerHttpResponse response) {
        userService.updateUserDescription(userId,cityName);
        return userService.updateCitySubscription(userId, cityName);
    }

    @GetMapping("/user-details/{userId}")
    public Mono<Tuple2<SubscriptionEntity, UserEntity>> getSubscriptionAndUser(@PathVariable Long userId) {
        return userService.getSubscriptionAndUser(userId);
    }

    @GetMapping("/city-list")
    public Flux<CityEntity> getCitiesList() {
        return cityService.getAllCities();
    }
}



