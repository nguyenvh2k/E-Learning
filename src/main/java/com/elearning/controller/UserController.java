package com.elearning.controller;

import com.elearning.constant.SystemConstant;
import com.elearning.dto.*;
import com.elearning.entity.User;
import com.elearning.service.UserService;
import com.elearning.service.impl.RefreshTokenService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenService refreshTokenService;


    /**
     * Create new user
     * @param registerDTO
     * @return Object
     */
    @PostMapping("/user")
    public ResponseEntity<Object> createUser(@ModelAttribute RegisterDTO registerDTO){
        User user = userService.saveUser(registerDTO);
        RegisterAbstractResponse registerResponse = new RegisterAbstractResponse();
        registerResponse.setType(SystemConstant.TYPE_REGISTER);
        if (user != null){
            registerResponse.setSuccess(true);
            UserDTO dto = new UserDTO();
            dto.setUserId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            registerResponse.setUserDTO(dto);
            registerResponse.setSuccess(true);
            registerResponse.setCode(SystemConstant.CODE_200);
            registerResponse.setMessage("Success !");
        }else {
            registerResponse.setSuccess(false);
            registerResponse.setCode(SystemConstant.CODE_403);
            registerResponse.setMessage("Username or email existed !");
        }

        return new ResponseEntity<>(registerResponse,HttpStatus.OK);
    }



    /**
     * Get info user
     * @param authentication
     * @return Object
     */
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/user/info")
    public ResponseEntity<Object> getUserInfo(Authentication authentication){
        MyUser myUser = (MyUser) authentication.getPrincipal();
        System.out.println(authentication.getAuthorities());
        User user = userService.findById(myUser.getId());
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    /**
     * Change email
     *
     * @param email
     * @param principal
     * @return Object
     */
    @PutMapping("/user/email")
    public ResponseEntity<Object> changeEmail(@RequestParam(value = "email",required = true,defaultValue = "")String email,
                                              Principal principal){
        String username = principal.getName();
        User user= userService.findByUsername(username);
        boolean result = userService.updateEmail(email,user.getId());
        ChangeEmailDTOAbstract changeEmailDTO = new ChangeEmailDTOAbstract();
        changeEmailDTO.setType("ChangeEmail");
        if (!result){
            changeEmailDTO.setCode(SystemConstant.CODE_403);
            changeEmailDTO.setSuccess(false);
            changeEmailDTO.setMessage("Change email failed !");
        }else {
            changeEmailDTO.setCode(SystemConstant.CODE_200);
            changeEmailDTO.setSuccess(true);
            changeEmailDTO.setMessage("Change email successfully !");
        }
        return new ResponseEntity<>(changeEmailDTO,HttpStatus.OK);
    }

    /**
     * Change password
     * @param oldPassword
     * @param newPassword
     * @param authentication
     * @return Object
     */
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @PutMapping("/user/password")
    public ResponseEntity<Object> changePassword(@RequestParam(value = "oldPassword",required = true,defaultValue = "")String oldPassword,
                                                 @RequestParam(value = "newPassword",required = true,defaultValue = "")String newPassword,
                                                 Authentication authentication){
        MyUser myUser = (MyUser) authentication.getPrincipal();
        User user = userService.findById(myUser.getId());
        userService.updatePassword(user,oldPassword,newPassword);
        PasswordAbstractResponse passwordResponse = new PasswordAbstractResponse();
        passwordResponse.setSuccess(true);
        passwordResponse.setType("Change password");
        passwordResponse.setCode(SystemConstant.CODE_200);
        passwordResponse.setMessage("Change password successfully !");
        return new ResponseEntity<>(passwordResponse,HttpStatus.OK);
    }


}
