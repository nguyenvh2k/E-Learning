package com.elearning.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterResponse {
    private Boolean success;
    private Integer code;

    @JsonProperty("user")
    private UserDTO userDTO;

    private String message;
}
