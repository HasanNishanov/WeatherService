package com.company.model.dto;

import com.company.enums.ProfileStatus;
import com.company.model.entity.SubscriptionEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String token;
    private ProfileStatus role = ProfileStatus.USER;
    private List<SubscriptionEntity> subscriptions = new ArrayList<>();
}
