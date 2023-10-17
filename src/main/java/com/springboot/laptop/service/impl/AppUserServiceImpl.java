package com.springboot.laptop.service.impl;

import com.springboot.laptop.base.AppUser;
import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.mapper.UserMapper;
import com.springboot.laptop.model.*;
import com.springboot.laptop.model.dto.AddressDTO;
import com.springboot.laptop.model.dto.UserDTO;
import com.springboot.laptop.model.dto.request.*;
import com.springboot.laptop.model.dto.response.*;
import com.springboot.laptop.model.enums.UserRoleEnum;
import com.springboot.laptop.model.jwt.JwtRequest;
import com.springboot.laptop.model.jwt.JwtResponse;
import com.springboot.laptop.repository.AccountRepository;
import com.springboot.laptop.repository.ResetTokenRepository;
import com.springboot.laptop.repository.CustomerRepository;
import com.springboot.laptop.security.services.UserDetailServiceImpl;
import com.springboot.laptop.service.MailService;
import com.springboot.laptop.service.UserRoleService;
import com.springboot.laptop.service.AppUserService;
import com.springboot.laptop.utils.JwtUtility;
import com.springboot.laptop.utils.ResetPasswordUtils;
import com.springboot.laptop.utils.UidUtils;
import freemarker.template.Configuration;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    @Value("${DEPLOY_FRONTEND}")
    private static String deployFrontend;

    public static final String F_DDMMYYYYHHMM = "dd/MM/yyyy HH:mm";
    private static final String RESET_PASSWORD_TEMPLATE_NAME = "reset_password.ftl";
    private static final String FROM = "Laptop Store<%s>";
    private static final String SUBJECT = "QUÊN MẬT KHẨU";


    private static String DOMAIN_CLIENT = deployFrontend + "/account/reset-password";
    private static final long MINUS_TO_EXPIRED = 10;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final ResetTokenRepository resetTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailServiceImpl userDetailService;
    private final JwtUtility jwtUtility;

    private final UserMapper userMapper;

    @Value("${spring.mail.username}")
    private String emailown;

    private final Configuration configuration;

    private final JavaMailSender mailSender;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public static String toStr(Date date, String format) {
        return date != null ? DateFormatUtils.format(date, format) : "";
    }

    private static Map buildModel(Customer customer, ResetTokenEntity resetToken) {
        Map<String, Object> model = new HashMap<>();
        model.put("time", toStr(new Date(), F_DDMMYYYYHHMM));
        model.put("name", customer.getName());
        model.put("email", customer.getEmail());
        model.put("link", String.format(DOMAIN_CLIENT + "/%s", resetToken.getToken()));
        model.put("minusToExpired", MINUS_TO_EXPIRED);
        return model;
    }


    @Override
    public Object authenticateUser(JwtRequest jwtRequest) {
        Map<String, Object> responseDTO = new HashMap<String, Object>();
        if (!StringUtils.hasText(jwtRequest.getUsername()) || !StringUtils.hasText(jwtRequest.getPassword()))
            throw new CustomResponseException(StatusResponseDTO.INFORMATION_IS_MISSING);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final UserDetails userDetails
                    = userDetailService.loadUserByUsername(jwtRequest.getUsername());

            AppUser loggedUser = null;
            if (userDetails.getAuthorities().iterator().next().getAuthority().equals("ROLE_CUSTOMER")) {

                loggedUser = customerRepository.findByUsernameIgnoreCase(userDetails.getUsername()).orElseThrow(() -> new RuntimeException(StatusResponseDTO.USER_NOT_FOUND.name()));
            } else
                loggedUser = accountRepository.findByUsernameIgnoreCase(userDetails.getUsername()).orElseThrow(() -> new RuntimeException(StatusResponseDTO.USER_NOT_FOUND.name()));


            if (!loggedUser.getEnabled()) throw new CustomResponseException(StatusResponseDTO.ACCOUNT_BEEN_INACTIVATED);

            final String tokenDto = jwtUtility.createToken(loggedUser);
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setJwtToken(tokenDto);
            jwtResponse.setExpiresIn((System.currentTimeMillis() + JwtUtility.EXPIRE_DURATION));

            if (loggedUser instanceof Customer) {

                jwtResponse.setRole("CUSTOMER");
            } else {
                boolean isAdmin = ((Account) loggedUser).getRoles().stream().anyMatch(role -> role.getName().equals(UserRoleEnum.ROLE_ADMIN));
                if (isAdmin) {
                    jwtResponse.setRole("ADMIN");
                } else {
                    jwtResponse.setRole("EMPLOYEE");
                }
            }
            responseDTO.put("loginInformation", jwtResponse);
            return responseDTO;
        } catch (BadCredentialsException e) {
            throw new CustomResponseException(StatusResponseDTO.FAIL_AUTHENTICATION);
        }

    }

    @Override
    public Object register(AppClientSignUpDTO user) throws Exception {

        if(!StringUtils.hasText(user.getPassword().trim())) throw new CustomResponseException(StatusResponseDTO.PASSWORD_NOT_PROVIDED);

        if(user.getPassword().length() <= 6) throw new CustomResponseException(StatusResponseDTO.PASSWORD_NOT_MEET_REQUIREMENT);

        if (customerRepository.existsByUsername(user.getUsername()))
            throw new CustomResponseException(StatusResponseDTO.USERNAME_IN_USE);

        if (customerRepository.existsByEmail(user.getEmail()))
            throw new CustomResponseException(StatusResponseDTO.EMAIL_IN_USE);

        if (!user.getPassword().equals(user.getRePassword()))
            throw new CustomResponseException(StatusResponseDTO.PASSWORD_NOT_MATCH);

        Customer appClient = new Customer();
        appClient.setUsername(user.getUsername());
        appClient.setEmail(user.getEmail());
        appClient.setPassword(passwordEncoder.encode(user.getPassword()));
        appClient.setEnabled(true);
        appClient.setCreatedTimestamp(new Date());

        try {
            return userMapper.userToUserDTO(customerRepository.save(appClient));
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public boolean userExists(String username, String email) {
        Optional<Customer> byUsername = customerRepository.findByUsernameIgnoreCase(username);
        Optional<Customer> byEmail = customerRepository.findByEmail(email);
        return byUsername.isPresent() || byEmail.isPresent();
    }

    @Override
    public Object deleteCustomer(Long customerId) {
        Customer existingUser = customerRepository.findById(customerId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND));
        try {
            customerRepository.delete(existingUser);
            return "Xoá thành công";
        } catch (Exception ex) {
            throw ex;
        }

    }

    @Override
    public Object updateStatus(Long customerId, String status) {
        try {
            Boolean updateStatus = status.equalsIgnoreCase("enabled");
            customerRepository.findById(customerId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND));
            ;
            customerRepository.updateStatus(customerId, updateStatus);
            return "Successfully";
        } catch (Exception ex) {
            throw ex;
        }
    }

    // use modelMapper instead - update later
    @Override
    public Customer updateInformation(UpdateInformationDTO userRequest) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer user = customerRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND));

        if (!Objects.isNull(userRequest.getName())) {
            user.setName(userRequest.getName());
        }
        if (!Objects.isNull(userRequest.getPhoneNumber())) {

            user.setPhoneNumber(userRequest.getPhoneNumber());
        }
        if (!Objects.isNull(userRequest.getImgURL())) {
            user.setImgURL(userRequest.getImgURL());
        }
        try {
            return customerRepository.save(user);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public Customer addNewAddress(AddressDTO requestDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer user = customerRepository.findByUsernameIgnoreCase(username).get();

        List<Address> userAddress = user.getAddresses();
        Address newAddress = Address.builder().address(requestDTO.getAddress()).city(requestDTO.getCity()).country(requestDTO.getCountry()).zipcode(requestDTO.getZipcode()).phoneNumber(requestDTO.getPhoneNumber()).customer(user).build();
        userAddress.add(newAddress);
        return (customerRepository.save(user));

    }


    @Override
    public Customer findUserByUserName(String username) {
        return customerRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND));

    }

    @Override
    public Object sendVerificationEmail(String email) throws IOException {

        if (!StringUtils.hasText(email)) {
            throw new CustomResponseException(StatusResponseDTO.EMAIL_NOT_BLANK);
        }

        Customer existingUser = customerRepository.findByEmail(email).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND));
        ;
        String token = UidUtils.generateRandomString(9);

        ResetTokenEntity resetToken = ResetTokenEntity.builder().token(token).userId(existingUser).active(true).createdDate(new Date()).build();

        resetTokenRepository.save(resetToken);

        freemarker.template.Template template = configuration.getTemplate(RESET_PASSWORD_TEMPLATE_NAME);

        Map model = buildModel(existingUser, resetToken);
        try {
            ResetPasswordMailDTO rpmd = ResetPasswordMailDTO.builder().from(String.format(FROM, email))
                    .to(existingUser.getEmail())
                    .text(FreeMarkerTemplateUtils.processTemplateIntoString(template, model))
                    .subject(SUBJECT).build();
            mailService.sendWForgotPassword(rpmd).get();
        } catch (Exception e) {
            throw new CustomResponseException(StatusResponseDTO.INTERNAL_SERVER);
        }
        return existingUser;
    }

    @Override
    public Object updatePassword(NewPasswordRequest newPasswordRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer userExist = customerRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND));

        if (!passwordEncoder.matches(newPasswordRequest.getOldPassword(), userExist.getPassword()))
            throw new RuntimeException("Mật khẩu cũ không đúng !");

        if (!StringUtils.hasText(newPasswordRequest.getNewPassword()) || !StringUtils.hasText(newPasswordRequest.getRetypeNewPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu không được để trống");

        if (!newPasswordRequest.getNewPassword().equals(newPasswordRequest.getRetypeNewPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hai mật khẩu không trùng nhau");
        userExist.setPassword(passwordEncoder.encode(newPasswordRequest.getNewPassword()));
        return userMapper.userToUserDTO(customerRepository.save(userExist));

    }


    @Override
    public Object logoutUser(HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.hasText(request.getHeader("Authorization")) && request.getHeader("Authorization").startsWith("Bearer ")) {
            String token = request.getHeader("Authorization").substring(7);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {

                new SecurityContextLogoutHandler().logout(request, response, null);
            }
            return "Logout thành công";
        } else
            return "Không có token";
    }


    @Override
    public Object getUserInformation() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer user = customerRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND));
        return userMapper.userToUserDTO(user);
    }

    @Override
    public Object resetPassword(ResetPasswordDTO payload) throws Exception {
        try {
            ResetPasswordUtils.validConstraint(payload);

            ResetTokenEntity resetToken = resetTokenRepository.findByToken(payload.getToken()).orElseThrow(() -> new RuntimeException("Token is not valid !"));
            if (!payload.getPassword().equals(payload.getRetypePassword()))
                throw new RuntimeException("Password is not matching");
            Customer existingUser = customerRepository.findById(resetToken.getUserId().getId()).get();

            existingUser.setPassword(passwordEncoder.encode(payload.getPassword()));

            resetToken.setActive(false);
            resetTokenRepository.save(resetToken);
            return existingUser;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
