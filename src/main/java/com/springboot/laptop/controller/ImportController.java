package com.springboot.laptop.controller;


import com.springboot.laptop.model.dto.request.ImportRequestDTO;
import com.springboot.laptop.service.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/imports")
public class ImportController {

    private final ImportService importService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public Object createImport(@RequestBody ImportRequestDTO importRequest) {
        return ResponseEntity.ok().body(importService.createImport(importRequest));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public Object getAllImport() {
        return ResponseEntity.ok().body(importService.getAllImport());
    }

}
