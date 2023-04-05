package com.example.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class UserDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private String nickName;

    private String icon;
}
