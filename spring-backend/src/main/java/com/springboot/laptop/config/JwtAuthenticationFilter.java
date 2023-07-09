package com.springboot.laptop.config;

import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.model.dto.response.StatusResponseDTO;
import com.springboot.laptop.security.services.UserDetailServiceImpl;
import com.springboot.laptop.utils.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtility jwtTokenProvider;
    @Autowired
    private UserDetailServiceImpl customUserDetailService;

    @Autowired private JwtUtility jwtUtility;





//    only run into this after pass shouldNotFilter function
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

            System.out.println("Da vao filter internal");

            String token = getJwt(request);

            if(token !=null &&jwtUtility.validateToken(token)){
                String username = jwtUtility.getUerNameFromToken(token);

                UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        catch(Exception ex) {
            throw new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND);
        }
        filterChain.doFilter(request,response);
    }
    private String getJwt(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if(authHeader !=null && authHeader.startsWith("Bearer")){
            return authHeader.replace("Bearer", "");
        }
        return null;
    }

}