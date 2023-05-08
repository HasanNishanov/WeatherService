package com.company.service;

import com.company.model.entity.SubscriptionEntity;
import com.company.model.entity.UserEntity;
import com.company.repository.SubscriptionRepository;
import com.company.repository.UserRepository;
import org.jboss.resteasy.spi.UnauthorizedException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
public class AdminService {
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private static final String ADMIN_TOKEN = "admin_token";

    public void verifyToken(String token) {
        if (!ADMIN_TOKEN.equals(token)) {
            throw new UnauthorizedException("Invalid admin token");
        }
    }
    public AdminService(UserRepository userRepository, SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
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

}
