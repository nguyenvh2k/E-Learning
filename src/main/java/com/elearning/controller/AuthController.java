package com.elearning.controller;

import com.elearning.constant.SystemConstant;
import com.elearning.dto.request.LoginDTO;
import com.elearning.dto.request.TokenRefreshRequest;
import com.elearning.dto.response.LoginAbstractResponseDTO;
import com.elearning.dto.response.TokenRefreshResponse;
import com.elearning.entity.MyUser;
import com.elearning.entity.RefreshToken;
import com.elearning.entity.User;
import com.elearning.exception.TokenRefreshException;
import com.elearning.jwt.JwtTokenProvider;
import com.elearning.repository.RoleRepository;
import com.elearning.service.impl.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
        LoginAbstractResponseDTO loginResponseDTO = new LoginAbstractResponseDTO();
        loginResponseDTO.setType(SystemConstant.TYPE_LOGIN);
        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken((MyUser) authentication.getPrincipal());
            MyUser myUser = (MyUser) authentication.getPrincipal();
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(myUser.getId());
            loginResponseDTO.setMessage("Login successfully !");
            loginResponseDTO.setCode(SystemConstant.CODE_200);
            loginResponseDTO.setSuccess(true);
            loginResponseDTO.setAccessToken(jwt);
            loginResponseDTO.setRefreshToken(refreshToken.getToken());
        }catch (Exception e){
            loginResponseDTO.setCode(SystemConstant.CODE_401);
            loginResponseDTO.setMessage("Username or password are incorrect !");
        }
            return new ResponseEntity<>(loginResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/auth/json")
    public ResponseEntity<Object> authenticateUserVerJson(@ModelAttribute LoginDTO loginDto){
        Authentication authentication = null;
        LoginAbstractResponseDTO loginResponseDTO = new LoginAbstractResponseDTO();
        loginResponseDTO.setType("Login");
        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken((MyUser) authentication.getPrincipal());
            loginResponseDTO.setMessage("Login successfully !");
            loginResponseDTO.setCode(SystemConstant.CODE_200);
            loginResponseDTO.setSuccess(true);
            loginResponseDTO.setAccessToken(jwt);
        }catch (Exception e){
            loginResponseDTO.setCode(SystemConstant.CODE_401);
            loginResponseDTO.setMessage("Username or password are incorrect !");
        }
            return new ResponseEntity<>(loginResponseDTO, HttpStatus.OK);
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

    @GetMapping("/auth/logout")
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
