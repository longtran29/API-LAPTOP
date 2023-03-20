package com.springboot.laptop.controller;


import com.springboot.laptop.model.Address;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.dto.request.AddressRequestDTO;
import com.springboot.laptop.model.dto.request.AppClientSignUpDTO;
import com.springboot.laptop.model.dto.request.NewPasswordRequest;
import com.springboot.laptop.model.dto.request.TokenDTO;
import com.springboot.laptop.model.dto.response.ErrorCode;
import com.springboot.laptop.model.dto.response.ResponseDTO;
import com.springboot.laptop.model.dto.response.SuccessCode;
import com.springboot.laptop.model.dto.response.UserInformationDTO;
import com.springboot.laptop.model.enums.UserRoleEnum;
import com.springboot.laptop.model.jwt.JwtRequest;
import com.springboot.laptop.model.jwt.JwtResponse;
import com.springboot.laptop.repository.UserRepository;
import com.springboot.laptop.security.services.UserDetailServiceImpl;
import com.springboot.laptop.service.UserServiceImpl;
import com.springboot.laptop.utils.JwtUtility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:3000", allowCredentials="true")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1")
public class AuthController {
    private final UserServiceImpl userServiceImpl;

    private final AuthenticationManager authenticationManager;
    private final JwtUtility jwtUtility;
    private final UserDetailServiceImpl userDetailService;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(UserServiceImpl userServiceImpl, UserRepository userRepository, JwtUtility jwtUtility, UserDetailServiceImpl userDetailService, AuthenticationManager authenticationManager) {
        this.userServiceImpl = userServiceImpl;
        this.authenticationManager = authenticationManager;
        this.jwtUtility = jwtUtility;
        this.userDetailService = userDetailService;
        this.userRepository = userRepository;
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
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),

                            jwtRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            final UserDetails userDetails
                    = userDetailService.loadUserByUsername(jwtRequest.getUsername());

            UserEntity loggedUser = userServiceImpl.findUserByUserName(userDetails.getUsername());

            final String tokenDto = jwtUtility.createToken(loggedUser);
            JwtResponse jwtResponse  = new JwtResponse();
            jwtResponse.setJwtToken(tokenDto);

            if(loggedUser.getRoles().stream().anyMatch(role -> role.getName().equals(UserRoleEnum.ROLE_USER.name()))) {
                jwtResponse.setRole("USER");
            }
            else {
                jwtResponse.setRole("ADMIN");
            }
            responseDTO.setSuccessCode(SuccessCode.LOGIN_SUCCESS);
            responseDTO.setData(jwtResponse);
        } catch (BadCredentialsException e) {
            responseDTO.setErrorCode(ErrorCode.FAIL_AUTHENTICATION);
            responseDTO.setData("Tên đăng nhập hoặc mật khẩu sai !");
            return ResponseEntity.badRequest().body(responseDTO);
        }
        return ResponseEntity.ok(responseDTO);
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
        System.out.println("Vao day trươc " + requestAddress);
        UserEntity user = userServiceImpl.addNewAddress(requestAddress);
        return ResponseEntity.ok((getUserInformation()));
    }

    @GetMapping(value = "/user/information")
    public ResponseEntity<?> getUserInformation() {
        ResponseDTO responseDTO = new ResponseDTO();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username).get();

        List<Address> addresses = user.getAddresses();
        UserInformationDTO userInfo = UserInformationDTO.builder().addresses(addresses).username(user.getUsername()).email(user.getEmail()).imgURL(user.getImgURL()).phoneNumber(user.getPhoneNumber()).name(user.getName()).build();
//                userInfo.setAddresses(addresses);
//        userInfo.setEmail(user.getEmail());
        responseDTO.setData(userInfo);
        return ResponseEntity.ok(responseDTO);

    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        if(StringUtils.hasText(request.getHeader("Authorization")) && request.getHeader("Authorization").startsWith("Bearer ")){
            String token =request.getHeader("Authorization").substring(7);

            System.out.println("token = " + token);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null){
                System.out.println("Da vao trong nay " + auth );
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
            return ResponseEntity.ok("Logout success");
        }
        else
            return ResponseEntity.ok("không có token");
    }


    @PostMapping("/newpassword/{email}")
    public ResponseEntity<?> newPassword(@PathVariable("email") String email,@Valid @RequestBody NewPasswordRequest newPasswordRequest) {
        return ResponseEntity.ok(userServiceImpl.newPassword(email, newPasswordRequest.getPassword(), newPasswordRequest.getPasswordConfirm()));
    }
    @PostMapping("/forgetpassword/{email}")
    public ResponseEntity<?> forgetPasword(@PathVariable("email") String email, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException, MessagingException, UnsupportedEncodingException {
        String verifyURL = request.getRequestURL().toString()
                .replace(request.getServletPath(), "") + "/signin/newpassword/"+email;
        userServiceImpl.sendVerificationEmail(email,verifyURL);
        return ResponseEntity.ok("Đã gửi mail");
    }
}
