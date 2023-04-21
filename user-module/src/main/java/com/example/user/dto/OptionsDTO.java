package com.example.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class OptionsDTO {

    private String value;

    private String label;

    private List<OptionsDTO> children;
}
