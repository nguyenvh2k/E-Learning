package com.elearning.service;

import com.elearning.dto.request.PasswordReset;
import com.elearning.dto.request.RegisterDTO;
import com.elearning.entity.User;

public interface UserService {
    User findById(Long userId);
    User saveUser(RegisterDTO registerDTO);
    User findByUsername(String username);
    Boolean checkExistUserName(String username);
    Boolean checkExistUsernameOrEmail(String username,String email);
    PasswordReset resetPasswordByEmail(String email);
    Boolean checkEmailExist(String email);
    boolean updateEmail(String email,Long userId);
    void updatePassword(User user, String oldPassword, String newPassword);
    void updatePasswordByEmail(String userPass,String email);
}
