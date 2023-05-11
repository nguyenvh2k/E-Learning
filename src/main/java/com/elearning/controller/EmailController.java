package com.elearning.controller;

import com.elearning.constant.SystemConstant;
import com.elearning.dto.PasswordReset;
import com.elearning.dto.ResponseDTOs;
import com.elearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {
    @Autowired
    private UserService userService;
    @PostMapping()
    public ResponseEntity<Object> sendCode(@RequestParam("email")String email){
        PasswordReset result = userService.resetPasswordByEmail(email);
        ResponseDTOs<PasswordReset> responseDTOs;
        if (result.getCode()==0){
            responseDTOs =
                    new ResponseDTOs<>(false, SystemConstant.CODE_404,result,"Email does not exist !");
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
         responseDTOs =
                new ResponseDTOs<>(true, SystemConstant.CODE_200,result,"Send code successfully !");
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    @PutMapping("/password")
    public ResponseEntity<Object> resetPassword(@RequestParam("email")String email,@RequestParam("newPassword")String newPassword){
        ResponseDTOs<String> responseDTOs;
        userService.updatePasswordByEmail(newPassword,email);
        responseDTOs =
                new ResponseDTOs<>(true, SystemConstant.CODE_200,"","Reset password successfully !");
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

}
