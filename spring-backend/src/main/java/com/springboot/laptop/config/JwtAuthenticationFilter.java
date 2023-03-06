package com.springboot.laptop.config;

import com.springboot.laptop.security.services.UserDetailServiceImpl;
import com.springboot.laptop.utils.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtility jwtTokenProvider;
    @Autowired
    private UserDetailServiceImpl customUserDetailService;

    @Autowired private JwtUtility jwtUtility;

    private static List<String> skipFilterUrls = Arrays.asList("/api/v1/categories", "/api/v1/authenticate", "/api/v1/login");

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        System.out.println("In not filter");
        return skipFilterUrls.stream().anyMatch(url -> new AntPathRequestMatcher(url).matches(request));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException
    {

        String accessToken = jwtUtility.resolveAccessToken(request);
//        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NzYyOTE4ODgsImV4cCI6MTY3NjI5MzY4OCwic3ViIjoiYWRtaW4ifQ.QMV7ewBDKBkz62ji3-crRs4GZ4cHgYMoGnFt0yLyxJM";
        String refreshToken = jwtUtility.resolveRefreshToken(request);


        System.out.println("Token i doFilter is " + accessToken + " and " + refreshToken);

        if (accessToken != null) {
            System.out.println("Da vao accessToken");
            // AccessToken 이 유효하면?
            if (jwtTokenProvider.validateToken(accessToken)) {
                System.out.println("Validated");
                this.setAuthentication(accessToken);
            }
            // AccessToken 은 만료, RefreshToken 은 존재
            else if(!jwtTokenProvider.validateToken(accessToken) && refreshToken != null) {

                //RefreshToken 유효?
                boolean validRefreshToken = jwtUtility.validateToken(refreshToken);

                //RefreshToken DB에 존재?
                boolean isRefreshToken = jwtUtility.existsRefreshToken(refreshToken);

                //RefreshToken이 유효기간 남았고 DB에 남아있다면 AccessToken 새로 발급
                if (validRefreshToken && isRefreshToken) {
                    String userName = jwtUtility.getUsernameFromToken(refreshToken);
                    String newAccessToken = jwtUtility.createToken(userName);
                    jwtUtility.setHeaderAccessToken(response, newAccessToken);
                    this.setAuthentication(newAccessToken);
                }
            }
        }
        System.out.println("Truoc filter in Filter Jwer");
        filterChain.doFilter(request, response);
    }
    private String getJwtToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            System.out.println("Token is " + bearerToken.substring(7, bearerToken.length()));
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    public void setAuthentication(String token) {
        Authentication authentication = jwtUtility.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}