package com.elearning.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterResponse extends ResponseDTO{
    @JsonProperty("user")
    private UserDTO userDTO;

}
