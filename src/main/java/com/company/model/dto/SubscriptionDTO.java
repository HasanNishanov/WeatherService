package com.company.model.dto;

import com.company.model.entity.CityEntity;
import com.company.model.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionDTO {

    private Long id;
    private UserEntity user_id;
    private CityEntity city_id;
}
