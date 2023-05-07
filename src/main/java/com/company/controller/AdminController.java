package com.company.controller;

import com.company.model.entity.UserEntity;
import com.company.service.AdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
@RequestMapping("/admin/api/v1")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @GetMapping("/users")
    public Flux<UserEntity> getUserList(@RequestHeader("Authorization") String token) {
        adminService.verifyToken(token);
        return adminService.getUserList();
    }
}
