package com.elearning.service.impl;

import com.elearning.dto.MyUser;
import com.elearning.dto.RoleName;
import com.elearning.entity.Role;
import com.elearning.entity.User;
import com.elearning.repository.RoleRepository;
import com.elearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user==null){
            throw new UsernameNotFoundException("User not found !");
        }

//        List<RoleName> rolesOfUser = roleRepository.findRoleById(user.getId());
        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getUserRoles().forEach(userRole -> {
            authorities.add(new SimpleGrantedAuthority(userRole.getRole().getName()));
        });
        MyUser myUser = new MyUser(user.getUsername(),user.getPassword(),true,true,true,true,authorities);
        myUser.setId(user.getId());
        return myUser;
    }

    public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
        User user = userRepository.findById(userId).orElse(null);
        if (user==null){
            throw new UsernameNotFoundException("User not found");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getUserRoles().forEach(userRole -> {
            authorities.add(new SimpleGrantedAuthority(userRole.getRole().getName()));
        });
        MyUser myUser = new MyUser(user.getUsername(),user.getPassword(),true,true,true,true,authorities);
        myUser.setId(user.getId());
        return myUser;
    }

}
