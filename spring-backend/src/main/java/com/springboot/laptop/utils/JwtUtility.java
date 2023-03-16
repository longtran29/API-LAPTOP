package com.springboot.laptop.utils;
import com.springboot.laptop.model.RefreshToken;
import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.dto.request.TokenDTO;
import com.springboot.laptop.repository.RefreshTokenRepository;
import com.springboot.laptop.security.services.UserDetailServiceImpl;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtility implements Serializable {

//    30 minutes (30 * 60 seconds = 1800 seconds) - 1000 * 60 * 30
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;

    private static final String BEARER_PREFIX = "Bearer ";
    @Autowired private RefreshTokenRepository refreshTokenRepository;

    @Autowired private UserDetailServiceImpl userDetailService;
    @Autowired private RefreshTokenRepository tokenRepository;
    @Value("${jwt.secret}")
    private String secret;



    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes())).parseClaimsJws(token).getBody().getSubject();
    }

    // HS512 method
    public TokenDTO doGenerateToken(UserEntity user) {
        Date now = new Date();


        Date accessTokenExpiresIn = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
//                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // (1)
                .setIssuedAt(now) // 발급시간(iat)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME)) // 만료시간(exp)
                .setSubject(user.getUsername()) //  토큰 제목(subject)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secret.getBytes())) // 알고리즘, 시크릿 키
                .compact();
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .setSubject(user.getUsername())
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secret.getBytes()))
                .compact();

        RefreshToken refreshTokenObject = RefreshToken.builder()
                .id(user.getId())
                .user(user)
                .value(refreshToken)
                .build();

        refreshTokenRepository.save(refreshTokenObject);
        return TokenDTO.builder()
                .grantType(BEARER_PREFIX)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    public String createToken(String userName) {
        Date now = new Date();

        return Jwts.builder()
//                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // (1)
                .setIssuedAt(now) // 발급시간(iat)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME)) // 만료시간(exp)
                .setSubject(userName) //  토큰 제목(subject)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secret.getBytes())) // 알고리즘, 시크릿 키
                .compact();
    }

    //AccessToken response
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        // exposure the response headers all fields
        response.addHeader("Access-Control-Expose-Headers", "*");
        response.setHeader("Authorization", "Bearer " + accessToken);
    }

    public String resolveAccessToken(HttpServletRequest request) {
        System.out.println("Da vao resolve aceestoken");
        if(request.getHeader("Authorization") != null)
            // noting: check if authorization string starts with Bearer suffix or not
            return request.getHeader("Authorization");
        return null;
    }


    //Request Header 에서 RefreshToken 값 추출
    public String resolveRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        System.out.println("Cookies is " + cookies);

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    System.out.println("Cookie is " + cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    //RefreshToken response
    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/"); //쿠키의 유효범위 추후 서비스 발전시 쿠키의 범위 설정 필요
        cookie.setMaxAge(7 * 24 * 60 * 60);

        System.out.println("Da vao setHeader cookie");

        response.addCookie(cookie);
    }

    public Authentication getAuthentication (String token) {
        UserDetails userDetails = userDetailService.loadUserByUsername(this.getUsernameFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes())).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean existsRefreshToken(String refreshToken) {
        return tokenRepository.existsByValue(refreshToken);
    }

//    @Transactional
//    @Modifying
//    public void logout(HttpServletResponse response, String refreshToken) {
//
//        Member member = memberRepository.findByRefreshToken(refreshToken);
//        member.setRefreshToken(null);
//
//        Cookie cookie = new Cookie("refreshToken", null);
//
//        cookie.setSecure(true);
//        cookie.setHttpOnly(true);
//        cookie.setPath("/");
//
//        response.addCookie(cookie);
//    }
}
