package com.springboot.laptop.controller;


import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.dto.*;
import com.springboot.laptop.model.enums.UserRoleEnum;
import com.springboot.laptop.model.jwt.JwtRequest;
import com.springboot.laptop.model.jwt.JwtResponse;
import com.springboot.laptop.repository.UserRepository;
import com.springboot.laptop.security.services.UserDetailServiceImpl;
import com.springboot.laptop.service.UserService;
import com.springboot.laptop.utils.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
//@CrossOrigin(origins = "http://localhost:3000", allowCredentials="true")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1")
public class AuthController {
    private final UserService userService;

    private final AuthenticationManager authenticationManager;
    private final JwtUtility jwtUtility;
    private final UserDetailServiceImpl userDetailService;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(UserService userService, UserRepository userRepository, JwtUtility jwtUtility, UserDetailServiceImpl userDetailService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtility = jwtUtility;
        this.userDetailService = userDetailService;
        this.userRepository = userRepository;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> signup(@RequestBody AppClientSignUpDto user) throws Exception {
        ResponseDTO responseDTO = new ResponseDTO();
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is exist"));
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error : Email is exist"));
        }
        UserEntity client = this.userService.register(user);
        responseDTO.setData(client);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/authenticate")
    public ResponseEntity authenticate(@RequestBody JwtRequest jwtRequest, HttpServletResponse response) throws Exception {
        try {
            System.out.println("Da vao authenticate roi ne username " + jwtRequest.getUsername() + " password " + jwtRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("auth.getAuthorities() = " + authentication.getAuthorities());
            System.out.printf("Info are " + jwtRequest.getUsername() + jwtRequest.getPassword());
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        final UserDetails userDetails
                = userDetailService.loadUserByUsername(jwtRequest.getUsername());

        UserEntity loggedUser = userService.findUserByUserName(userDetails.getUsername());

        final TokenDto tokenDto = jwtUtility.doGenerateToken(loggedUser);
        jwtUtility.setHeaderAccessToken(response, tokenDto.getAccessToken());
        jwtUtility.setHeaderRefreshToken(response, tokenDto.getRefreshToken());
        JwtResponse jwtResponse  = new JwtResponse();
        jwtResponse.setJwtToken(tokenDto.getAccessToken());
        if(loggedUser.getRoles().stream().anyMatch(role -> role.getName().equals(UserRoleEnum.ROLE_USER.name()))) {
            jwtResponse.setRole("USER");
        }
        else {
            jwtResponse.setRole("ADMIN");
        }
        return ResponseEntity.ok(jwtResponse);
    }
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        if(StringUtils.hasText(request.getHeader("Authorization")) && request.getHeader("Authorization").startsWith("Bearer ")){
            String token =request.getHeader("Authorization").substring(7);

            System.out.println("token = " + token);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null){
                System.out.println("Da vao trong nay " + auth );
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
            return ResponseEntity.ok("Logout success");
        }
        else
            return ResponseEntity.ok("không có token");
    }


    @PostMapping("/newpassword/{email}")
    public ResponseEntity<?> newPassword(@PathVariable("email") String email,@Valid @RequestBody NewPasswordRequest newPasswordRequest) {
        return ResponseEntity.ok(userService.newPassword(email, newPasswordRequest.getPassword(), newPasswordRequest.getPasswordConfirm()));
    }
    @PostMapping("/forgetpassword/{email}")
    public ResponseEntity<?> forgetPasword(@PathVariable("email") String email, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException, MessagingException, UnsupportedEncodingException {
        String verifyURL = request.getRequestURL().toString()
                .replace(request.getServletPath(), "") + "/signin/newpassword/"+email;
        userService.sendVerificationEmail(email,verifyURL);
        return ResponseEntity.ok("Đã gửi mail");
    }
}
