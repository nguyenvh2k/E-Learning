package com.elearning.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDTO {

    private Long userId;

    @JsonProperty("displayName")
    private String username;

    private String email;
}
