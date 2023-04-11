package com.elearning.controller;

import com.elearning.dto.LoginDTO;
import com.elearning.dto.LoginResponseDTO;
import com.elearning.dto.MyUser;
import com.elearning.jwt.JwtTokenProvider;
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
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/auth")
    public ResponseEntity<Object> authenticateUser(@ModelAttribute LoginDTO loginDto){
        Authentication authentication = null;
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setType("Login");
        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken((MyUser) authentication.getPrincipal());
            loginResponseDTO.setMessage("Login successfully !");
            loginResponseDTO.setCode(200);
            loginResponseDTO.setSuccess(true);
            loginResponseDTO.setAccessToken(jwt);
        }catch (Exception e){
            loginResponseDTO.setCode(401);
            loginResponseDTO.setMessage("Username or password are incorrect !");
        }finally {
            return new ResponseEntity<>(loginResponseDTO, HttpStatus.OK);
        }
    }

    @PostMapping("/auth/json")
    public ResponseEntity<Object> authenticateUserVerJson(@RequestBody LoginDTO loginDto){
        Authentication authentication = null;
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setType("Login");
        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken((MyUser) authentication.getPrincipal());
            loginResponseDTO.setMessage("Login successfully !");
            loginResponseDTO.setCode(200);
            loginResponseDTO.setSuccess(true);
            loginResponseDTO.setAccessToken(jwt);
        }catch (Exception e){
            loginResponseDTO.setCode(401);
            loginResponseDTO.setMessage("Username or password are incorrect !");
        }finally {
            return new ResponseEntity<>(loginResponseDTO, HttpStatus.OK);
        }
    }


}
