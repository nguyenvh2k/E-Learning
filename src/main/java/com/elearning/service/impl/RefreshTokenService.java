package com.elearning.service.impl;

import com.elearning.entity.RefreshToken;
import com.elearning.exception.TokenRefreshException;
import com.elearning.repository.RefreshTokenRepository;
import com.elearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${jwt.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
         RefreshToken refreshTokenExist = refreshTokenRepository.findByUserId(userId);
        if (refreshTokenExist == null){
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setUser(userRepository.findById(userId).get());
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken = refreshTokenRepository.save(refreshToken);
            return refreshToken;
        }else {
            refreshTokenExist.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            refreshTokenExist.setToken(UUID.randomUUID().toString());
            refreshTokenExist = refreshTokenRepository.save(refreshTokenExist);
            return refreshTokenExist;
        }
    }

    public RefreshToken verifyExpiration(RefreshToken token) throws Exception {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(),"Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }
}