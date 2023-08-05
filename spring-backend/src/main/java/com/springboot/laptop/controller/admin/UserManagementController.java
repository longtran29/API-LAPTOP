package com.springboot.laptop.controller.admin;


import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.dto.response.StatusResponseDTO;
//import com.springboot.laptop.service.impl.UserServiceImpl;
import com.springboot.laptop.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserManagementController {

    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<?> managementUser() {
        return ResponseEntity.ok().body(userService.getAll());
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
