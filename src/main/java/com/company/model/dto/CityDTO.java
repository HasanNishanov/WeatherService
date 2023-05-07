package com.company.model.dto;


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

}
