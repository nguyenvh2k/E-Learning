package com.elearning.service.impl;

import com.elearning.constant.SystemConstant;
import com.elearning.entity.Role;
import com.elearning.repository.RoleRepository;
import com.elearning.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public Role save(String roleName) {
        Role role = new Role();
        role.setName(SystemConstant.ROLE_MEMBER);
        roleRepository.save(role);
        return role;
    }

    @Override
    public Role checkExistByName(String roleName) {
        return roleRepository.findByName(roleName);
    }
}
