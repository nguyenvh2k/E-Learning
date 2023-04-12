package com.elearning.jwt;


import com.elearning.dto.MyUser;
import com.elearning.service.impl.CustomUserDetailsService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenProvider {
    private final String JWT_SECRET = "@hueyyrhjfnjd73";
    //Thời gian có hiệu lực của chuỗi jwt
    private final  long JWT_EXPIRATION = 604800000L;

    private final  long JWT_REFRESH_EXPIRATION = 6048000000L;

    //tao ra jwt tu thong tin user
    public String generateToken(MyUser myUser){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime()+JWT_EXPIRATION);

        //tao ra chuoi jwt web token tu id cua user
        return Jwts.builder()
                .setSubject(Long.toString(myUser.getId()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512,JWT_SECRET)
                .compact();
    }

    public String generateTokenById(Long userId){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime()+JWT_REFRESH_EXPIRATION);
        return Jwts.builder()
                .setSubject(Long.toString(userId))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512,JWT_SECRET)
                .compact();
    }

    //lay thong tin user tu jwt
    public Long getUserIdFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken){
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        }catch (MalformedJwtException ex){
            log.error("Invalid JWT token");
        }catch (ExpiredJwtException ex){
            log.error("Expired JWT token");
        }catch (UnsupportedJwtException ex){
            log.error("Unsupported JWT token");
        }catch (IllegalArgumentException ex){
            log.error("JWT claims string is empty");
        }
        return false;
    }

}
