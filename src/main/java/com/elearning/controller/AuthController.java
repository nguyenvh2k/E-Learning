package com.elearning.controller;

import com.elearning.dto.LoginDTO;
import com.elearning.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<Object> authenticateUser(@RequestBody LoginDTO loginDto){
        Authentication authentication = null;
        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(), loginDto.getPassword()));
        }catch (Exception e){
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setMessage("Username and password incorrect !");
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setMessage("Login successful !");
        loginResponse.setUserInfo(authentication.getPrincipal());
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<Object> authorizedUser(){
        return new ResponseEntity<>("Authorized !", HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return new ResponseEntity<>("Logout success !", HttpStatus.OK);
    }
}
