package com.elearning.listener;

import com.elearning.entity.Role;
import com.elearning.entity.User;
import com.elearning.entity.UserRole;
import com.elearning.repository.RoleRepository;
import com.elearning.repository.UserRepository;
import com.elearning.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


public class DataSeedingListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (roleRepository.findByName("ADMIN") == null) {
            Role role = new Role();
            role.setName("ADMIN");
            roleRepository.save(role);
        }

        if (roleRepository.findByName("MEMBER") == null) {
            Role role = new Role();
            role.setName("MEMBER");
            roleRepository.save(role);
        }
        // Admin account
        if (userRepository.findByUsername("tester") == null) {
            User admin = new User();
            admin.setUsername("tester");
            admin.setFirstName("Nguyên");
            admin.setPassword(passwordEncoder.encode("123456"));
            userRepository.save(admin);
            List<UserRole> userRoles = new ArrayList<>();
            UserRole userRole1 = new UserRole();
            userRole1.setUser(admin);
            userRole1.setRole(roleRepository.findByName("ADMIN"));
            userRoles.add(userRole1);
            UserRole userRole2 = new UserRole();
            userRole2.setUser(admin);
            userRole2.setRole(roleRepository.findByName("MEMBER"));
            userRoles.add(userRole2);
            userRoleRepository.saveAll(userRoles);
        }

    }

}
