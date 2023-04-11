package com.elearning.controller;

import com.elearning.dto.*;
import com.elearning.entity.User;
import com.elearning.jwt.JwtTokenProvider;
import com.elearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseEntity<Object> createUser(@ModelAttribute RegisterDTO registerDTO){
        User user = userService.saveUser(registerDTO);
        RegisterResponse registerResponse = new RegisterResponse();
        if (user != null){
            registerResponse.setSuccess(true);
            UserDTO dto = new UserDTO();
            dto.setUserId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            registerResponse.setUserDTO(dto);
            registerResponse.setSuccess(true);
            registerResponse.setCode(200);
            registerResponse.setMessage("Success !");
        }else {
            registerResponse.setSuccess(false);
            registerResponse.setCode(403);
            registerResponse.setMessage("Username or email existed !");
        }

        return new ResponseEntity<>(registerResponse,HttpStatus.OK);
    }


    @GetMapping("/user/info")
    public ResponseEntity<Object> getUserInfo(Principal principal){
        String username = principal.getName();
        User user = userService.findByUsername(username);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

}
