package com.elearning.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterAbstractResponse extends AbstractResponseDTO {
    @JsonProperty("data")
    private UserDTO userDTO;

}
