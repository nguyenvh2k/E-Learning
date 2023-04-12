package com.elearning.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class ResponseDTO {
    private Boolean success;
    private Integer code;
    private String type;
    private String message;
}
