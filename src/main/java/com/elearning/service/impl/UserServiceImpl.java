package com.elearning.service.impl;

import com.elearning.constant.SystemConstant;
import com.elearning.dto.RegisterDTO;
import com.elearning.entity.Role;
import com.elearning.entity.User;
import com.elearning.exception.PasswordIncorrectException;
import com.elearning.repository.UserRepository;
import com.elearning.service.RoleService;
import com.elearning.service.UserRoleService;
import com.elearning.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    /**
     * Hàm insert user
     * Nếu role member chưa tồn tại add role vào
     * => add User
     * => add role vào User
     *
     * @param dto
     * @return User
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public User saveUser(RegisterDTO dto) {
        Random rd = new Random();

        Boolean userNameExist = checkExistUsernameOrEmail(dto.getUsername(),dto.getEmail());
        if (userNameExist){
            return null;
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setCode(rd.nextInt(1000000));
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);

        Role role = roleService.checkExistByName(SystemConstant.ROLE_MEMBER);
        if (role == null){
            role = roleService.save(SystemConstant.ROLE_MEMBER);
        }

        userRoleService.saveUserRole(user,role);

        return user;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Boolean checkExistUserName(String username) {
        User user = userRepository.findByUsername(username);
        if (user!=null){
            return true;
        }
        return false;
    }

    @Override
    public Boolean checkExistUsernameOrEmail(String username, String email) {
        User user = userRepository.findByUsernameOrEmail(username,email);
        if (user!=null){
            return true;
        }
        return false;
    }

    /**
     * Update email
     *
     * @param email
     * @param userId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public boolean updateEmail(String email, Long userId) {
        try {
            userRepository.updateEmail(email,userId);
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    /**
     * Change password
     *
     * @param user
     * @param oldPassword
     * @param newPassword
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updatePassword(User user, String oldPassword, String newPassword) {
        boolean result = passwordEncoder.matches(oldPassword,user.getPassword());
        if (!result){
            throw new PasswordIncorrectException();
        }
        userRepository.updatePassword(passwordEncoder.encode(newPassword),user.getId());
    }

}
