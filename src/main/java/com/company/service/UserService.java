package com.company.service;

import com.company.enums.ProfileStatus;
import com.company.model.dto.UserDTO;
import com.company.model.entity.SubscriptionEntity;
import com.company.model.entity.UserEntity;
import com.company.repository.CityRepository;
import com.company.repository.SubscriptionRepository;
import com.company.repository.UserRepository;
import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final SubscriptionRepository subscriptionRepository;
    private ModelMapper modelMapper;

    public UserService(UserRepository userRepository, CityRepository cityRepository, SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public String encode(String password) {
        BCryptPasswordEncoder b = new BCryptPasswordEncoder();
        return b.encode(password);
    }

    public Mono<String> subscribeToCity(Long userId, String cityName) {
        return cityRepository.findByName(cityName)
                .switchIfEmpty(Mono.error(new NotFoundException("City not found")))
                .flatMap(city -> userRepository.findById(userId)
                        .switchIfEmpty(Mono.error(new NotFoundException("User not found")))
                        .flatMap(user -> {
                            user.setSubscriptions(cityName);
                            return userRepository.save(user)
                                    .flatMap(savedUser -> {
                                        // Создаем новую запись в таблице Subscriptions
                                        SubscriptionEntity subscription = new SubscriptionEntity();
                                        subscription.setUser_id(userId);
                                        subscription.setCity_name(city.getName());
                                        return subscriptionRepository.save(subscription);
                                    })
                                    .thenReturn("Successfully subscribed to city " + cityName);
                        }));
    }

    public Mono<Object> createUser(UserEntity user) {
        return userRepository.findByUsername(user.getUsername())
                .flatMap(foundUser -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already taken")))
                .switchIfEmpty(Mono.defer(() -> {
                    UserEntity userEntity = modelMapper.map(user, UserEntity.class);
                    userEntity.setPassword(encode(userEntity.getPassword()));
                    userEntity.setRole(ProfileStatus.USER);
                    return userRepository.save(userEntity)
                            .map(savedUser -> modelMapper.map(savedUser, UserDTO.class));
                }))
                .onErrorResume(throwable -> Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create user")));
    }


    public Mono<Object> updateUser(Long userId, UserDTO userDTO) {
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

    public Mono<Void> deleteUser(Long userId) {
        return userRepository.deleteById(userId);
    }
}


