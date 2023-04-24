package com.springboot.laptop.controller;


import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.exception.UserPasswordException;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.dto.request.*;
import com.springboot.laptop.model.dto.response.*;
import com.springboot.laptop.model.jwt.JwtRequest;
import com.springboot.laptop.repository.UserRepository;
import com.springboot.laptop.security.services.UserDetailServiceImpl;
import com.springboot.laptop.service.impl.CloudinaryService;
import com.springboot.laptop.service.impl.UserServiceImpl;
import com.springboot.laptop.utils.JwtUtility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
//@CrossOrigin(origins = "http://localhost:3000", allowCredentials="true")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1")
public class AuthController {
    private final UserServiceImpl userServiceImpl;

    private final AuthenticationManager authenticationManager;
    private final JwtUtility jwtUtility;
    private final UserDetailServiceImpl userDetailService;
    private final UserRepository userRepository;

    private final CloudinaryService cloudinaryService;

    @Autowired
    public AuthController(UserServiceImpl userServiceImpl, UserRepository userRepository, JwtUtility jwtUtility, UserDetailServiceImpl userDetailService, AuthenticationManager authenticationManager, CloudinaryService cloudinaryService) {
        this.userServiceImpl = userServiceImpl;
        this.authenticationManager = authenticationManager;
        this.jwtUtility = jwtUtility;
        this.userDetailService = userDetailService;
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
    }



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
    public ResponseEntity<?> signup(@RequestBody AppClientSignUpDTO user) throws Exception {
        ResponseDTO responseDTO = new ResponseDTO();
        if (userRepository.existsByUsername(user.getUsername())) {
            responseDTO.setErrorCode(ErrorCode.EXIST_USER);
            responseDTO.setData("Tên người dùng đã tồn tại !");
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            responseDTO.setErrorCode(ErrorCode.EXIST_USER);
            responseDTO.setData("Gmail đã được sử dụng !");
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
        UserEntity client = this.userServiceImpl.register(user);
        responseDTO.setData(client);
        responseDTO.setSuccessCode(SuccessCode.REIGSTER_SUCCESS);
        return ResponseEntity.ok(responseDTO);
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
    public ResponseEntity<?> authenticate(@RequestBody JwtRequest jwtRequest, HttpServletResponse response) throws Exception {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            return ResponseEntity.ok().body(userServiceImpl.authenticateUser(jwtRequest));
        } catch (CustomResponseException ex) {
            responseDTO.setData(ex.getReason());
            return ResponseEntity.badRequest().body(responseDTO);
        }
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
    @PostMapping("/user/addAddress")
    public ResponseEntity<?> addAddress(@RequestBody AddressRequestDTO requestAddress) {
        UserEntity user = userServiceImpl.addNewAddress(requestAddress);
        return ResponseEntity.ok((getUserInformation()));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping(value = "/user/information")
    public ResponseEntity<?> getUserInformation() {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(userServiceImpl.getUserInformation());
        return ResponseEntity.ok().body(responseDTO);

    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
       return ResponseEntity.ok(userServiceImpl.logoutUser(request,response));
    }

    @PostMapping("/upload")
    public String uploadFile(@Param("file") MultipartFile file) {
        String url = cloudinaryService.uploadFile(file);
        return url;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping("/updateInformation")
    public ResponseEntity<?> updateInformation(@RequestBody UserRequestDTO userRequestDTO) {
            return ResponseEntity.ok().body(userServiceImpl.updateInformation(userRequestDTO));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/resetPassword")
    public ResponseEntity<?> newPassword(@Valid @RequestBody NewPasswordRequest newPasswordRequest) throws UserPasswordException {
        ResponseDTO response = new ResponseDTO();

        try {
            response.setData(userServiceImpl.newPassword(newPasswordRequest));
            return ResponseEntity.ok(response);
        } catch (UserPasswordException ex) {
            response.setData(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception ex) {
            response.setData(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/forgotPassword/{email}")
    public ResponseEntity<?> forgetPassword(@PathVariable("email") String email) throws Exception {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userServiceImpl.sendVerificationEmail(email));
        }
        // handler more exceptions
        catch(ResponseStatusException ex) {
            return ResponseEntity.badRequest().body(ex.getReason());
        }
        catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO payload) throws Exception {
        try {
            return ResponseEntity.ok().body(userServiceImpl.resetPassword(payload));
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
}
