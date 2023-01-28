package com.springboot.laptop.handler;


import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.dto.AppClientSignUpDto;
import com.springboot.laptop.model.enums.UserRoleEnum;
import com.springboot.laptop.model.jwt.JwtRequest;
import com.springboot.laptop.model.jwt.JwtResponse;
import com.springboot.laptop.security.services.UserDetailServiceImpl;
import com.springboot.laptop.service.UserService;
import com.springboot.laptop.utils.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;


    private final AuthenticationManager authenticationManager;
    private final JwtUtility jwtUtility;
    private final UserDetailServiceImpl userDetailService;

    @Autowired
    public UserController(UserService userService, JwtUtility jwtUtility, UserDetailServiceImpl userDetailService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtility = jwtUtility;
        this.userDetailService = userDetailService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> signup(@RequestBody AppClientSignUpDto user) throws Exception {

        System.out.println(user);
        if (this.userService.userExists(user.getUsername(), user.getEmail())) {
            throw new RuntimeException("Username or email address already in use.");
        }
        UserEntity client = this.userService.register(user);
        return new ResponseEntity<UserEntity>(client, HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception {
        try {
            System.out.println("Da vao authenticate roi ne username " + jwtRequest.getUsername() + " password " + jwtRequest.getPassword());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
            System.out.printf("Info are " + jwtRequest.getUsername() + jwtRequest.getPassword());
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        final UserDetails userDetails
                = userDetailService.loadUserByUsername(jwtRequest.getUsername());

        final String token = jwtUtility.generateToken(userDetails);

        System.out.println("Token is " + token);
        return new JwtResponse(token);

    }

//    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "Requestor-Type", exposedHeaders = "X-Get-Header")
    @PostMapping("/login")
    public String logInUser(@RequestParam String username) throws Exception {
        System.out.println("Da vao logInUser");
        UserEntity user = userService.findUserByUserName(username);
        if(user.getRoles().stream().anyMatch(role -> role.getRole().equals(UserRoleEnum.ROLE_USER))) {
            System.out.println("USER ROLE NHA");
            return "USER";
        }
        else {
            return "ADMIN";
        }
    }


}
