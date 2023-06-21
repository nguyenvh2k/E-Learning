package com.elearning.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractResponseDTO {
    private Boolean success;
    private Integer code;
    private String type;
    private String message;
}
