package com.springboot.laptop.utils;
import com.springboot.laptop.model.RefreshToken;
import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.dto.request.TokenDTO;
import com.springboot.laptop.repository.RefreshTokenRepository;
import com.springboot.laptop.security.services.UserDetailServiceImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Base64;
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
            logger.error("Invalid JWT signature -> Message: {}",e);
        } catch (MalformedJwtException e){
            logger.error("Invalid format Token -> Message: {}",e);
        } catch (ExpiredJwtException e){
            logger.error("Expired JWT token -> Message: {}",e);
        } catch (UnsupportedJwtException e){
            logger.error("Unsupported JWT token -> Message: {}",e);
        } catch (IllegalArgumentException e){
            logger.error("JWT claims string is empty --> Message {}",e);
        }
        return false;
    }
    public String getUerNameFromToken(String token){
        String userName = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
        return userName;
    }
}
