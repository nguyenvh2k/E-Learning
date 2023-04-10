package com.elearning.service.impl;

import com.elearning.entity.Role;
import com.elearning.entity.User;
import com.elearning.entity.UserRole;
import com.elearning.repository.UserRoleRepository;
import com.elearning.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    @Transactional
    public void saveUserRole(User user, Role role) {
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRoleRepository.save(userRole);
    }
}
