package com.springboot.laptop.handler;


import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.dto.AppClientSignUpDto;
import com.springboot.laptop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {


    @Autowired
    private UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<?> signup(@RequestBody AppClientSignUpDto user) throws Exception {
        System.out.println(user);
        if (this.userService.userExists(user.getUsername(), user.getEmail())) {
            throw new RuntimeException("Username or email address already in use.");
        }
        UserEntity client = this.userService.register(user);
        return new ResponseEntity<UserEntity>(client, HttpStatus.CREATED);
    }


}
