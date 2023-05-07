package com.company.service;

import com.company.enums.ProfileStatus;
import com.company.model.dto.UserDTO;
import com.company.model.entity.UserEntity;
import com.company.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
public class UserService {


    private final UserRepository userRepository;


    private ModelMapper modelMapper;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String encode(String password) {

        BCryptPasswordEncoder b = new BCryptPasswordEncoder();

        return b.encode(password);
    }



    public Mono<Object> createUser(UserEntity user) {
        return userRepository.findByUsername(user.getUsername())
                .flatMap(foundUser -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already taken")))
                .switchIfEmpty(Mono.defer(() -> {
                    UserEntity userEntity = modelMapper.map(user, UserEntity.class);
                    userEntity.setPassword(encode(userEntity.getPassword()));
                    userEntity.setRole(ProfileStatus.USER);
                    userEntity.setSubscriptions(new ArrayList<>());
                    return userRepository.save(userEntity)
                            .map(savedUser -> modelMapper.map(savedUser, UserDTO.class));
                }))
                .onErrorResume(throwable -> Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create user")));
    }


    public Mono<Object> updateUser(String userId, UserDTO userDTO) {
        return userRepository.findByUsername(userDTO.getUsername())
                .flatMap(foundUser -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already taken")))
                .switchIfEmpty(Mono.defer(() -> {
                    return userRepository.findById(userId)
                            .flatMap(userEntity -> {
                                userEntity.setUsername(userDTO.getUsername());
                                userEntity.setPassword(encode(userDTO.getPassword()));
                                userEntity.setRole(userDTO.getRole());
                                userEntity.setSubscriptions(userDTO.getSubscriptions());
                                return userRepository.save(userEntity);
                            })
                            .map(savedUser -> modelMapper.map(savedUser, UserDTO.class));
                }))
                .onErrorResume(throwable -> Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update user")));
    }

    public Mono<Void> deleteUser(String userId) {
        return userRepository.deleteById(userId);
    }
}


