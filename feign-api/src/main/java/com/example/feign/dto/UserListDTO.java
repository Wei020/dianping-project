package com.example.feign.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserListDTO {

    private List<Long> ids;

    private String idStr = "";
}
