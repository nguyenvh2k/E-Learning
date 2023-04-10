package com.elearning.service;

import com.elearning.dto.RegisterDTO;
import com.elearning.entity.User;

public interface UserService {
    User saveUser(RegisterDTO registerDTO);
    Boolean checkExistUserName(String username);
    Boolean checkExistUsernameOrEmail(String username,String email);
}
