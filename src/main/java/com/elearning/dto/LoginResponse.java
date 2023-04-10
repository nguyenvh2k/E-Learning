package com.elearning.dto;

import lombok.Data;


@Data
public class LoginResponse {
    private Object userInfo;
    private String message;
}
