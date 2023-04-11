package com.elearning.dto;

import lombok.Data;

@Data
public abstract class ResponseDTO {
    private Boolean success;
    private Integer code;
    private String type;
    private String message;
}
