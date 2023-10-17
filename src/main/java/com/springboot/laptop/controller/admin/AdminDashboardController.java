package com.springboot.laptop.controller.admin;


import com.springboot.laptop.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminService adminService;

    @Operation(summary = "API dashboard admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/dashboard")
    @SecurityRequirement(name = "bearerAuth")
    public Object dashboard () {
        return ResponseEntity.ok().body(adminService.dashboard());
    }
}
