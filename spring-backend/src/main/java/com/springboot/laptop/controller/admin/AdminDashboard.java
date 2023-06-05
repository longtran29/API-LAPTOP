package com.springboot.laptop.controller.admin;


import com.springboot.laptop.model.dto.request.ChangeStatusDTO;
import com.springboot.laptop.model.dto.response.ResponseDTO;
import com.springboot.laptop.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminDashboard {

    private final AdminService adminService;

    @Autowired
    public AdminDashboard(AdminService adminService) {
        this.adminService = adminService;

    }

    @Operation(summary = "API dashboard admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/dashboard")
    @SecurityRequirement(name = "bearerAuth")
    public Object dashboard () {
        return ResponseEntity.ok().body(adminService.dashboard());
    }
}
