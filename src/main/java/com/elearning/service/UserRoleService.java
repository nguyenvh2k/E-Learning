package com.elearning.service;

import com.elearning.entity.Role;
import com.elearning.entity.User;

public interface UserRoleService {
    void saveUserRole(User user, Role role);
}
