package com.elearning.service;

import com.elearning.dto.RegisterDTO;
import com.elearning.entity.User;
import lombok.extern.java.Log;

public interface UserService {
    User findById(Long userId);
    User saveUser(RegisterDTO registerDTO);
    User findByUsername(String username);
    Boolean checkExistUserName(String username);
    Boolean checkExistUsernameOrEmail(String username,String email);
    boolean updateEmail(String email,Long userId);
}
