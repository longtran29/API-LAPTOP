package com.springboot.laptop.utils;
import com.springboot.laptop.exception.JwtValidationException;
import com.springboot.laptop.model.UserEntity;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
public class JwtUtility implements Serializable {
    @Value("${jwt.secret}")
    private String jwtSecret;
    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hour

    private static final Logger logger = LoggerFactory.getLogger(JwtUtility.class);

    public String createToken(UserEntity user){
//        UserEntity userPrinciple = (UserEntity) authentication.getPrincipal();
        return Jwts.builder().setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS256,jwtSecret )
                .compact();
    }
    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e){
            throw new JwtValidationException("Invalid JWT signature", e);
        } catch (MalformedJwtException e){
            throw new JwtValidationException("Invalid format Token", e);
        } catch (ExpiredJwtException e){
            throw new JwtValidationException("Expired JWT token", e);
        } catch (UnsupportedJwtException e){
            throw new JwtValidationException("Unsupported JWT token", e);
        } catch (IllegalArgumentException e){
            throw new JwtValidationException("JWT claims string is empty", e);
        }
    }
    public String getUerNameFromToken(String token){
        String userName = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
        return userName;
    }
}
