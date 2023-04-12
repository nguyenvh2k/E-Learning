package com.elearning.repository;

import com.elearning.entity.RefreshToken;
import com.elearning.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    RefreshToken findByUserId(Long userId);
    @Modifying
    int deleteByUser(User user);
}
