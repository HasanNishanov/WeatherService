package com.company.service;

import com.company.model.entity.UserEntity;
import com.company.repository.UserRepository;
import org.jboss.resteasy.spi.UnauthorizedException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AdminService {
    private final UserRepository userRepository;
    private static final String ADMIN_TOKEN = "admin_token";

    public void verifyToken(String token) {
        if (!ADMIN_TOKEN.equals(token)) {
            throw new UnauthorizedException("Invalid admin token");
        }
    }
    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Flux<UserEntity> getUserList() {
        return userRepository.findAll();
    }
}
