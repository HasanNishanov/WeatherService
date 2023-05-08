package com.company.service;

import com.company.model.dto.UserDTO;
import com.company.model.entity.CityEntity;
import com.company.model.entity.SubscriptionEntity;
import com.company.model.entity.UserEntity;
import com.company.repository.CityRepository;
import com.company.repository.SubscriptionRepository;
import com.company.repository.UserRepository;
import org.jboss.resteasy.spi.UnauthorizedException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
public class AdminService {
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private ModelMapper modelMapper;
    private final SubscriptionRepository subscriptionRepository;
    private static final String ADMIN_TOKEN = "admin_token";
    public String encode(String password) {
        BCryptPasswordEncoder b = new BCryptPasswordEncoder();
        return b.encode(password);
    }
    public void verifyToken(String token) {
        if (!ADMIN_TOKEN.equals(token)) {
            throw new UnauthorizedException("Invalid admin token");
        }
    }
    public AdminService(UserRepository userRepository, CityRepository cityRepository, SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public Flux<UserEntity> getUserList() {
        return userRepository.findAll();
    }

    public Mono<Tuple2<SubscriptionEntity, UserEntity>> getSubscriptionAndUser(Long userId) {
        Mono<SubscriptionEntity> subscriptionMono = subscriptionRepository.findById(userId);
        Mono<UserEntity> userMono = userRepository.findById(userId);
        return Mono.zip(subscriptionMono, userMono);
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
                .onErrorResume(throwable -> Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update subscription-updated.html.html")));
    }


}
