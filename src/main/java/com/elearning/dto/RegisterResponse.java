package com.elearning.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class RegisterResponse {
    private Boolean success;
    private Object data;
    private String message;
}
