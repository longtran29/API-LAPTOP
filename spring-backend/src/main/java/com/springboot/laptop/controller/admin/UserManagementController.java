package com.springboot.laptop.controller.admin;


import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.dto.response.ResponseDTO;
import com.springboot.laptop.repository.UserRepository;
import com.springboot.laptop.service.UserServiceImpl;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
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
        ResponseDTO response = new ResponseDTO();
        try {
            userService.updateStatus(customerId, status);
            return ResponseEntity.ok().body("Cập nhật thành công");

        } catch (CustomResponseException ex) {
            response.setData(ex.getReason());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long customerId) throws Exception {
        ResponseDTO response = new ResponseDTO();
        try {
            userService.deleteCustomer(customerId);
            response.setData("Xoá thành công");
        } catch (CustomResponseException ex) {
            response.setData(ex.getReason());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception ex) {
            response.setData(ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok().body(response);
    }


}