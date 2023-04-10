package com.elearning.service;

import com.elearning.entity.Role;

public interface RoleService {
    Role save(String roleName);
    Role checkExistByName(String roleName);
}
