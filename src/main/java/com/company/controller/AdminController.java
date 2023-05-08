package com.company.controller;

import com.company.model.entity.SubscriptionEntity;
import com.company.model.entity.UserEntity;
import com.company.service.AdminService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;


@RestController
@RequestMapping("/admin/api/v1")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @GetMapping("/user-list")
    public Flux<UserEntity> getUserList(@RequestHeader("Authorization") String token) {
        adminService.verifyToken(token);
        return adminService.getUserList();
    }


    @GetMapping("/user-details/{userId}")
    public Mono<Tuple2<SubscriptionEntity, UserEntity>> getSubscriptionAndUser(@PathVariable Long userId) {
        return adminService.getSubscriptionAndUser(userId);
    }
}
