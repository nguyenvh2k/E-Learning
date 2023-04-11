package com.elearning.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private boolean success;
    private Integer code;
    private String accessToken;
    private String tokenType = "Bearer";
    private String message;
}
