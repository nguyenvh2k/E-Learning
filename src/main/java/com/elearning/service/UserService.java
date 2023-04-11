package com.elearning.service;

import com.elearning.dto.RegisterDTO;
import com.elearning.entity.User;

public interface UserService {
    User saveUser(RegisterDTO registerDTO);
    User findByUsername(String username);
    Boolean checkExistUserName(String username);
    Boolean checkExistUsernameOrEmail(String username,String email);
}
