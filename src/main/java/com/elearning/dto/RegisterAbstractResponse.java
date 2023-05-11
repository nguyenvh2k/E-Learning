package com.elearning.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterAbstractResponse extends AbstractResponseDTO {
    @JsonProperty("user")
    private UserDTO userDTO;

}
