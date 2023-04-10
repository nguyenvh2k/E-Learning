package com.elearning.controller;

import com.elearning.dto.RegisterDTO;
import com.elearning.dto.RegisterResponse;
import com.elearning.entity.User;
import com.elearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RegisterResponse registerResponse;

    @PostMapping("/user")
    public ResponseEntity<Object> createUser(@RequestBody RegisterDTO registerDTO){
        User user = userService.saveUser(registerDTO);

        if (user != null){
            registerResponse.setSuccess(true);
            registerResponse.setData(user);
            registerResponse.setMessage("Insert success !");
        }else {
            registerResponse.setSuccess(false);
            registerResponse.setData("");
            registerResponse.setMessage("Username or email existed !");
        }

        return new ResponseEntity<>(registerResponse,HttpStatus.OK);
    }

}
