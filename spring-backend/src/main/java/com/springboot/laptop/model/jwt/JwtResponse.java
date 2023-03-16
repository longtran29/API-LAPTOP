package com.springboot.laptop.model.jwt;


import com.springboot.laptop.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    private String jwtToken;
    private String role;

}
