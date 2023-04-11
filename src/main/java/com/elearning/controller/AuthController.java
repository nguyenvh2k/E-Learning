package com.elearning.controller;

import com.elearning.dto.LoginDTO;
import com.elearning.dto.LoginResponse;
import com.elearning.dto.MyUser;
import com.elearning.jwt.JwtTokenProvider;
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
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/auth")
    public ResponseEntity<Object> authenticateUser(@ModelAttribute LoginDTO loginDto){
        Authentication authentication = null;
        LoginResponse loginResponse = new LoginResponse();
        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken((MyUser) authentication.getPrincipal());
            loginResponse.setMessage("Login successfully !");
            loginResponse.setCode(200);
            loginResponse.setSuccess(true);
            loginResponse.setAccessToken(jwt);
        }catch (Exception e){
            loginResponse.setCode(401);
            loginResponse.setMessage("Username or password are incorrect !");
        }finally {
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        }
    }


}
