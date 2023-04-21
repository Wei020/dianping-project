package com.example.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProvinceDTO {

    private String provinceName;

    private List<CityDTO> citys;
}
