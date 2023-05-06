package com.company.model.dto;


import com.company.model.entity.WeatherEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class CityDTO {
    private Long id;
    private String name;
    private Double temperature;
    private Boolean visible;
    private List<WeatherEntity> weathers = new ArrayList<>();
}
