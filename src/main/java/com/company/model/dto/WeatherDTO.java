package com.company.model.dto;


import com.company.model.entity.CityEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WeatherDTO {
    private Long id;
    private Double temperature;
    private LocalDateTime datetime;
    private CityEntity city;

}
