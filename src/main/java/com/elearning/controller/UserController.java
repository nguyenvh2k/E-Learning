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

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;


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

    @PostMapping("/user/login")
    public ResponseEntity<Object> authenticateUser(@ModelAttribute LoginDTO loginDto){
        Authentication authentication = null;
        LoginResponse loginResponse = new LoginResponse();
        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken((MyUser) authentication.getPrincipal());
            loginResponse.setMessage("Login successful !");
            loginResponse.setCode(200);
            loginResponse.setSuccess(true);
            loginResponse.setAccessToken(jwt);
        }catch (Exception e){
            loginResponse.setCode(401);
            loginResponse.setMessage("Username and password incorrect !");
        }finally {
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        }
    }

}
