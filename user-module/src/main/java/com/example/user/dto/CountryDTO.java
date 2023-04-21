package com.example.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class CountryDTO {

    private List<ProvinceDTO> provinces;
}
