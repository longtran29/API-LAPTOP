package com.springboot.laptop.controller.admin;


import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.dto.response.StatusResponseDTO;
import com.springboot.laptop.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@SecurityRequirement(name = "bearerAuth")
public class UserManagementController {


    private final UserServiceImpl userService;

    @Autowired
    public UserManagementController(UserServiceImpl userService) {
        this.userService = userService;
    }




    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<?> managementUser() {
        List<UserEntity> users = userService.getAll();
        return ResponseEntity.ok().body(users);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{customerId}/{status}")
    public ResponseEntity<?> updateStatusUser(@PathVariable Long customerId, @PathVariable String status) {
        return ResponseEntity.ok().body(userService.updateStatus(customerId, status));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{customerId}")
    public Object deleteUser(@PathVariable Long customerId) throws Exception {
        return userService.deleteCustomer(customerId);
    }
}
