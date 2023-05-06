package com.springboot.laptop.service.impl;

import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.exception.UserPasswordException;
import com.springboot.laptop.model.Address;
import com.springboot.laptop.model.ResetTokenEntity;
import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.UserRoleEntity;
import com.springboot.laptop.model.dto.request.*;
import com.springboot.laptop.model.dto.response.*;
import com.springboot.laptop.model.enums.UserRoleEnum;
import com.springboot.laptop.model.jwt.JwtRequest;
import com.springboot.laptop.model.jwt.JwtResponse;
import com.springboot.laptop.repository.ResetTokenRepository;
import com.springboot.laptop.repository.UserRepository;
import com.springboot.laptop.security.services.UserDetailServiceImpl;
import com.springboot.laptop.service.MailService;
import com.springboot.laptop.service.UserService;
import com.springboot.laptop.utils.JwtUtility;
import com.springboot.laptop.utils.ResetPasswordUtils;
import com.springboot.laptop.utils.UidUtils;
import freemarker.template.Configuration;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    public static final String F_DDMMYYYYHHMM = "dd/MM/yyyy HH:mm";
    private static final String RESET_PASSWORD_TEMPLATE_NAME = "reset_password.ftl";
    private static final String FROM = "Laptop Store<%s>";
    private static final String SUBJECT = "QUÊN MẬT KHẨU";
    private static final String DOMAIN_CLIENT = "http://localhost:3000/reset-password";
    private static final long MINUS_TO_EXPIRED = 10;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleServiceImpl userRoleServiceImpl;
    private final ResetTokenRepository resetTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailServiceImpl userDetailService;
    private final JwtUtility jwtUtility;
    @Value("${spring.mail.username}")
    private String emailown;
    @Autowired
    private Configuration configuration;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    public UserServiceImpl(Configuration configuration, UserRepository userRepository, PasswordEncoder passwordEncoder, UserRoleServiceImpl userRoleServiceImpl, ResetTokenRepository resetTokenRepository, MailService mailService, AuthenticationManager authenticationManager, UserDetailServiceImpl userDetailService, JwtUtility jwtUtility) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleServiceImpl = userRoleServiceImpl;
        this.resetTokenRepository = resetTokenRepository;
        this.mailService = mailService;
        this.configuration = configuration;
        this.authenticationManager = authenticationManager;

        this.userDetailService = userDetailService;
        this.jwtUtility = jwtUtility;

    }

    public static String toStr(Date date, String format) {
        return date != null ? DateFormatUtils.format(date, format) : "";
    }

    private static Map buildModel(UserEntity customer, ResetTokenEntity resetToken) {
        Map<String, Object> model = new HashMap<>();
        model.put("time", toStr(new Date(), F_DDMMYYYYHHMM));
        model.put("name", customer.getName());
        model.put("email", customer.getEmail());
        model.put("link", String.format(DOMAIN_CLIENT + "/%s", resetToken.getToken()));
        model.put("minusToExpired", MINUS_TO_EXPIRED);
        return model;
    }

    public Object authenticateUser(JwtRequest jwtRequest) {
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

            UserEntity loggedUser = this.findUserByUserName(userDetails.getUsername());

            if (!loggedUser.getEnabled()) throw new CustomResponseException(StatusResponseDTO.ACCOUNT_BEEN_INACTIVATED);

            final String tokenDto = jwtUtility.createToken(loggedUser);
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setJwtToken(tokenDto);
            jwtResponse.setExpiresIn((System.currentTimeMillis() + JwtUtility.EXPIRE_DURATION));

            if (loggedUser.getRoles().stream().anyMatch(role -> role.getName().equals(UserRoleEnum.ROLE_USER.name()))) {
                jwtResponse.setRole("USER");
            } else {
                jwtResponse.setRole("ADMIN");
            }
            responseDTO.setSuccessCode(SuccessCode.LOGIN_SUCCESS);
            responseDTO.setData(jwtResponse);
            return responseDTO;
        } catch (CustomResponseException ex) {
            throw new CustomResponseException(StatusResponseDTO.ACCOUNT_BEEN_INACTIVATED);
        } catch (BadCredentialsException e) {
            throw new CustomResponseException(StatusResponseDTO.FAIL_AUTHENTICATION);
        }

    }

    @Override
    public UserEntity register(AppClientSignUpDTO user) throws Exception {
        UserRoleEntity userRole = this.userRoleServiceImpl.getUserRoleByEnumName(UserRoleEnum.ROLE_USER.name());
        if (!user.getPassword().equals(user.getRePassword()))
            throw new CustomResponseException(StatusResponseDTO.PASSWORD_NOT_MATCH);
        UserEntity appClient = new UserEntity();
        appClient.setRoles(List.of(userRole));
        appClient.setUsername(user.getUsername());
        appClient.setEmail(user.getEmail());
        appClient.setPassword(this.passwordEncoder.encode(user.getPassword()));
        appClient.setEnabled(true);
        return userRepository.save(appClient);
    }

    @Override
    public boolean userExists(String username, String email) {
        Optional<UserEntity> byUsername = this.userRepository.findByUsername(username);
        Optional<UserEntity> byEmail = this.userRepository.findByEmail(email);
        return byUsername.isPresent() || byEmail.isPresent();
    }

    @Override
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Object deleteCustomer(Long customerId) {
        UserEntity existingUser;

        if (userRepository.findById(customerId).isPresent()) {
            existingUser = userRepository.findById(customerId).get();
            if (existingUser.getOrders().size() > 0)
                throw new CustomResponseException(StatusResponseDTO.CUSTOMER_VIOLATION_EXCEPTION);
            userRepository.delete(existingUser);
            return "Xoá thành công";
        } else {
            throw new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND);
        }

    }

    @Override
    public void updateStatus(Long customerId, String status) {
        Boolean updateStatus = status.equalsIgnoreCase("enabled");
        UserEntity existingUser;
        if (userRepository.findById(customerId).isPresent()) {
            existingUser = userRepository.findById(customerId).get();
            userRepository.updateStatus(customerId, updateStatus);

        } else throw new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND);

    }

    // use modelMapper instead - update later
    @Override
    public UserEntity updateInformation(UserRequestDTO userRequest) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username).get();
        try {
            if (!userRequest.getName().isEmpty() && !Objects.isNull(userRequest.getName())) {
                user.setName(userRequest.getName());
            }
            if (!userRequest.getPhoneNumber().isEmpty() && !Objects.isNull(userRequest.getPhoneNumber())) {

                user.setPhoneNumber(userRequest.getPhoneNumber());
            }
            if (!userRequest.getImgURL().isEmpty() && !Objects.isNull(userRequest.getImgURL())) {
                user.setImgURL(userRequest.getImgURL());
            }
            return userRepository.save(user);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    public UserEntity addNewAddress(AddressRequestDTO requestAddress) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username).get();

        List<Address> userAddress = user.getAddresses();
        Address newAddress = new Address();
        newAddress.setAddress(requestAddress.getAddress());
        newAddress.setCity(requestAddress.getCity());
        newAddress.setCountry(requestAddress.getCountry());
        newAddress.setZipcode(requestAddress.getZipcode());
        newAddress.setPhoneNumber(requestAddress.getPhoneNumber());
        newAddress.setUser(user);
        userAddress.add(newAddress);

        return userRepository.save(user);
    }

    @Override
    public UserEntity findUserByUserName(String username) {
        UserEntity user = this.userRepository.findByUsername(username).get();
        return user;
    }

    @Override
    public Object sendVerificationEmail(String email) throws IOException {

        System.out.println("Go into send mail ");

        if (!StringUtils.hasText(email)) {
            // call StatusResponseDTO.EMAIL_NOT_BLANK -> return ("404", "Không tìm thấy dữ liệu") gồm code 404 và message
            throw new CustomResponseException(StatusResponseDTO.EMAIL_NOT_BLANK);
        }

        UserEntity existingUser;
        if (userRepository.findByEmail(email).isPresent()) {
            existingUser = userRepository.findByEmail(email).get();
        } else {
            throw new CustomResponseException(StatusResponseDTO.ERROR_NOT_FOUND);
        }
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
    public UserEntity newPassword(NewPasswordRequest newPasswordRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userExist = userRepository.findByUsername(username).get();

        if (StringUtils.hasText(newPasswordRequest.getNewPassword()) && StringUtils.hasText(newPasswordRequest.getRetypeNewPassword())) {
            if (newPasswordRequest.getNewPassword().equals(newPasswordRequest.getRetypeNewPassword())) {
                userExist.setPassword(passwordEncoder.encode(newPasswordRequest.getNewPassword()));
                userRepository.save(userExist);
                System.out.println("Đổi thành công thành công");
                return userExist;
            } else {
                throw new UserPasswordException("Hai mật khẩu không trùng nhau");
            }
        } else {
            throw new UserPasswordException("Không được để trống");
        }
    }


    @Override
    public Object logoutUser(HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.hasText(request.getHeader("Authorization")) && request.getHeader("Authorization").startsWith("Bearer ")) {
            String token = request.getHeader("Authorization").substring(7);

            System.out.println("token = " + token);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                System.out.println("Da vao trong nay " + auth);
                new SecurityContextLogoutHandler().logout(request, response, null);
            }
            return "Logout thành công";
        } else
            return "Không có token";
    }


    @Override
    public Object getUserInformation() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user;
        if (!userRepository.findByUsername(username).isPresent())
            throw new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND);
        else user = userRepository.findByUsername(username).get();

        List<Address> addresses = user.getAddresses();
        return UserInformationDTO.builder().addresses(addresses).username(user.getUsername()).email(user.getEmail()).imgURL(user.getImgURL()).phoneNumber(user.getPhoneNumber()).name(user.getName()).build();
    }

    @Override
    public Object resetPassword(ResetPasswordDTO payload) throws Exception {
        try {
            ResetPasswordUtils.validConstraint(payload);

            ResetTokenEntity resetToken = resetTokenRepository.findByToken(payload.getToken());
            UserEntity existingUser = userRepository.findById(resetToken.getUserId().getId()).get();

            existingUser.setPassword(passwordEncoder.encode(payload.getNewPassword()));

            resetToken.setActive(false);
            resetTokenRepository.save(resetToken);
            return existingUser;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
}
