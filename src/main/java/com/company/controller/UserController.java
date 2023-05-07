package com.company.controller;

import com.company.model.dto.UserDTO;
import com.company.model.entity.CityEntity;
import com.company.model.entity.UserEntity;
import com.company.repository.CityRepository;
import com.company.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Controller
public class UserController {

    private ModelMapper modelMapper;


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<?>> register(@RequestBody Mono<UserDTO> userDTOMono) {
        return userDTOMono
                .map(userDTO -> modelMapper.map(userDTO, UserEntity.class))
                .flatMap(userEntity -> userService.createUser(userEntity)
                        .map(jwtToken -> ResponseEntity.ok(Map.of("jwt", jwtToken))));
    }


}
