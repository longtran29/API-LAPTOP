package com.springboot.laptop.controller;


import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.AddressDTO;
import com.springboot.laptop.model.dto.ProductDTO;
import com.springboot.laptop.model.dto.request.*;
import com.springboot.laptop.model.jwt.JwtRequest;
import com.springboot.laptop.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {
    private final AppUserService userService;

    @Operation(summary = "Đăng ký ",
            description = "Đăng ký tài khoản mới",
            responses = {
                    @ApiResponse(description = "Đăng ký thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    @PostMapping("/auth/register")
    public ResponseEntity<?> signup(@Valid @RequestBody AppClientSignUpDTO user) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(user));
    }


    @Operation(summary = "Xác thực",
            description = "Xác thực đăng nhập và hệ thống trả về Bearer token và quyền",
            responses = {
                    @ApiResponse(description = "Xac thực thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody JwtRequest jwtRequest, HttpServletResponse response)  {
        return ResponseEntity.ok().body(userService.authenticateUser(jwtRequest));
    }


    @Operation(summary = "Thêm địa chỉ ",
            description = "Thêm đia chỉ mới ",
            responses = {
                    @ApiResponse(description = "Xac thực thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    @PostMapping("/user/add-address")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> addAddress(@RequestBody AddressDTO requestAddress) {
        return ResponseEntity.ok().body(userService.addNewAddress(requestAddress));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CUSTOMER', 'ROLE_EMPLOYEE')")
    @GetMapping(value = "/user/get-information")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getUserInformation() {
        return ResponseEntity.ok().body(userService.getUserInformation());

    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
       return ResponseEntity.ok(userService.logoutUser(request,response));
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PutMapping(path = "/user/update-profile", consumes = {   "multipart/form-data" })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> updateInformation(@RequestPart("user")  UpdateInformationDTO userRequestDTO, @RequestParam(value = "image", required = false) MultipartFile image) throws Exception {
            return ResponseEntity.ok().body(userService.updateInformation(userRequestDTO, image));
    }


    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/user/get-orders")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getOrders()  {
        return ResponseEntity.ok().body((userService.getUserOrders()));
    }

    @PreAuthorize ("permitAll")
    @PutMapping("/user/update-password")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> newPassword(@Valid @RequestBody NewPasswordRequest newPasswordRequest)  {
        return ResponseEntity.ok().body((userService.updatePassword(newPasswordRequest)));
    }

    @PutMapping("/forgot-password/{email}")
    public ResponseEntity<?> forgetPassword(@PathVariable("email") String email) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.sendVerificationEmail(email));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO payload) throws Exception {
        return ResponseEntity.ok().body(userService.resetPassword(payload));
    }
}
