package com.elearning.controller;

import com.elearning.dto.*;
import com.elearning.entity.RefreshToken;
import com.elearning.entity.User;
import com.elearning.exception.TokenRefreshException;
import com.elearning.jwt.JwtTokenProvider;
import com.elearning.repository.RoleRepository;
import com.elearning.service.impl.CustomUserDetailsService;
import com.elearning.service.impl.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private RoleRepository roleRepository;

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
            MyUser myUser = (MyUser) authentication.getPrincipal();
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(myUser.getId());
            loginResponseDTO.setMessage("Login successfully !");
            loginResponseDTO.setCode(200);
            loginResponseDTO.setSuccess(true);
            loginResponseDTO.setAccessToken(jwt);
            loginResponseDTO.setRefreshToken(refreshToken.getToken());
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

    @PostMapping("/auth/refreshtoken")
    public ResponseEntity<?> refreshToken(@ModelAttribute TokenRefreshRequest request) throws Exception {
        String requestRefreshToken = request.getRefreshToken();
        Optional<RefreshToken> refreshTokenOptional = refreshTokenService.findByToken(requestRefreshToken);
        if (!refreshTokenOptional.isPresent()){
            throw new TokenRefreshException(requestRefreshToken,"Token is invalid or expired !");
        }
         RefreshToken refreshToken =
                refreshTokenService.verifyExpiration(refreshTokenOptional.get());
        User user =refreshToken.getUser();
        String token = tokenProvider.generateTokenById(user.getId());
        return ResponseEntity.ok().body(new TokenRefreshResponse(token,requestRefreshToken));
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            refreshTokenService.deleteByUserId(((MyUser)auth.getPrincipal()).getId());
            return ResponseEntity.ok().body("Logout successfully !");
        }else {
            return ResponseEntity.notFound().build();
        }
    }

}
