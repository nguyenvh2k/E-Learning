package com.elearning.repository;

import com.elearning.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);

    User findByUsernameOrEmail(String username,String email);

    @Modifying
    @Query(value ="update users u set u.email=:userEmail where u.id=:userId " ,nativeQuery = true)
    void updateEmail(@Param("userEmail")String userEmail,@Param("userId")String userId);

    @Modifying
    @Query(value = "update users u set u.password=:userPass where u.id=:userId",nativeQuery = true)
    void updatePassword(@Param("userPass")String userPass,@Param("userId")String userId);
}
