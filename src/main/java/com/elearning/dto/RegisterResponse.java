package com.elearning.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class RegisterResponse extends ResponseDTO{
    @JsonProperty("user")
    private UserDTO userDTO;

}
