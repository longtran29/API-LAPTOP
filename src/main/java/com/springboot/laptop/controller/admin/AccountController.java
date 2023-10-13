package com.springboot.laptop.controller.admin;


import com.springboot.laptop.model.dto.request.UserCreationDTO;
//import com.springboot.laptop.service.impl.UserServiceImpl;
import com.springboot.laptop.service.AccountService;
import com.springboot.laptop.service.AppUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin/user_management")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/add", consumes = {"multipart/form-data" })
    public ResponseEntity<?> addNewUser(@RequestPart("user") UserCreationDTO userCreation, @RequestParam(value = "imageUser", required = false) MultipartFile imgUser) throws Exception {
        return ResponseEntity.ok().body(accountService.createUserForPrivilege(userCreation, imgUser));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<?> managementUser() {
        return ResponseEntity.ok().body(accountService.getAllAccount());
    }


//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PutMapping("/{customerId}/{status}")
//    public ResponseEntity<?> updateStatusUser(@PathVariable Long customerId, @PathVariable String status) {
//        return ResponseEntity.ok().body(userService.updateStatus(customerId, status));
//    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @DeleteMapping("/remove/{customerId}")
//    public Object deleteUser(@PathVariable Long customerId) throws Exception {
//        return userService.deleteCustomer(customerId);
//    }
}
