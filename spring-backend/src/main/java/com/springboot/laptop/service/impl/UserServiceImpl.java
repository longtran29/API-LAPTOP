package com.springboot.laptop.service.impl;

import com.springboot.laptop.controller.ImageUploadController;
import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.mapper.UserMapper;
import com.springboot.laptop.model.Address;
import com.springboot.laptop.model.ResetTokenEntity;
import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.UserRoleEntity;
import com.springboot.laptop.model.dto.AddressDTO;
import com.springboot.laptop.model.dto.UserDTO;
import com.springboot.laptop.model.dto.request.*;
import com.springboot.laptop.model.dto.response.*;
import com.springboot.laptop.model.enums.UserRoleEnum;
import com.springboot.laptop.model.jwt.JwtRequest;
import com.springboot.laptop.model.jwt.JwtResponse;
import com.springboot.laptop.repository.ResetTokenRepository;
import com.springboot.laptop.repository.UserRepository;
import com.springboot.laptop.security.services.UserDetailServiceImpl;
import com.springboot.laptop.service.MailService;
import com.springboot.laptop.service.UserRoleService;
import com.springboot.laptop.service.UserService;
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
public class UserServiceImpl implements UserService {

    public static final String F_DDMMYYYYHHMM = "dd/MM/yyyy HH:mm";
    private static final String RESET_PASSWORD_TEMPLATE_NAME = "reset_password.ftl";
    private static final String FROM = "Laptop Store<%s>";
    private static final String SUBJECT = "QUÊN MẬT KHẨU";
    private static final String DOMAIN_CLIENT = "http://localhost:3000/account/reset-password";
    private static final long MINUS_TO_EXPIRED = 10;
    private final UserRepository userRepository;
    private final UserRoleService userRoleServiceImpl;
    private final ResetTokenRepository resetTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailServiceImpl userDetailService;
    private final JwtUtility jwtUtility;

    private final CloudinaryService cloudinaryService;

    private final UserMapper userMapper;

    @Value("${spring.mail.username}")
    private String emailown;

    private final Configuration configuration;

    private final JavaMailSender mailSender;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


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


    @Override
    public Object authenticateUser(JwtRequest jwtRequest) {
       Map<String, Object> responseDTO = new HashMap<String, Object>();
       if(!StringUtils.hasText(jwtRequest.getUsername()) || !StringUtils.hasText(jwtRequest.getPassword()))
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

            UserEntity loggedUser = findUserByUserName(userDetails.getUsername());

            if (!loggedUser.getEnabled()) throw new CustomResponseException(StatusResponseDTO.ACCOUNT_BEEN_INACTIVATED);

            final String tokenDto = jwtUtility.createToken(loggedUser);
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setJwtToken(tokenDto);
            jwtResponse.setExpiresIn((System.currentTimeMillis() + JwtUtility.EXPIRE_DURATION));

            boolean checkRole = loggedUser.getRoles().stream().anyMatch(role -> role.getName().equals(UserRoleEnum.ROLE_ADMIN));
            if(checkRole)
                jwtResponse.setRole("ADMIN");
             else {
                jwtResponse.setRole("USER");
            }
            responseDTO.put("loginInformation",jwtResponse );
            return responseDTO;
        }  catch (BadCredentialsException e) {
            throw new CustomResponseException(StatusResponseDTO.FAIL_AUTHENTICATION);
        }

    }

    @Override
    public UserDTO register(AppClientSignUpDTO user) throws Exception {
        if (userRepository.existsByUsername(user.getUsername()))
            throw new CustomResponseException(StatusResponseDTO.USERNAME_IN_USE);

        if (userRepository.existsByEmail(user.getEmail()))
            throw new CustomResponseException(StatusResponseDTO.EMAIL_IN_USE);

        if (!user.getPassword().equals(user.getRePassword()))
            throw new CustomResponseException(StatusResponseDTO.PASSWORD_NOT_MATCH);

        UserRoleEntity userRole = userRoleServiceImpl.getUserRoleByEnumName(UserRoleEnum.ROLE_USER);

        UserEntity appClient = UserEntity.builder().roles(List.of(userRole)).username(user.getUsername()).email(user.getEmail()).password(passwordEncoder.encode(user.getPassword())).enabled(true).build();
        appClient.setCreatedTimestamp(new Date());

        try {
            return userMapper.userToUserDTO(userRepository.save(appClient));
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public boolean userExists(String username, String email) {
        Optional<UserEntity> byUsername = userRepository.findByUsernameIgnoreCase(username);
        Optional<UserEntity> byEmail = userRepository.findByEmail(email);
        return byUsername.isPresent() || byEmail.isPresent();
    }

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream().map(userMapper::userToUserDTO).collect(Collectors.toList());
    }

    @Override
    public Object deleteCustomer(Long customerId) {
        UserEntity existingUser = userRepository.findById(customerId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND));
        try {
            userRepository.delete(existingUser);
            return "Xoá thành công";
        } catch (Exception ex) {
            throw ex;
        }

    }

    @Override
    public Object updateStatus(Long customerId, String status) {
        try {
            Boolean updateStatus = status.equalsIgnoreCase("enabled");
            userRepository.findById(customerId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND));;
            userRepository.updateStatus(customerId, updateStatus);
            return "Successfully";
        } catch (Exception ex) {
            throw ex;
        }
    }

    // use modelMapper instead - update later
    @Override
    public UserEntity updateInformation(UpdateInformationDTO userRequest) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND));

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
            return userRepository.save(user);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public UserEntity addNewAddress(AddressDTO requestDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsernameIgnoreCase(username).get();

        List<Address> userAddress = user.getAddresses();
        Address newAddress = Address.builder().address(requestDTO.getAddress()).city(requestDTO.getCity()).country(requestDTO.getCountry()).zipcode(requestDTO.getZipcode()).phoneNumber(requestDTO.getPhoneNumber()).user(user).build();
        userAddress.add(newAddress);
        return (userRepository.save(user));

    }

    @Override
    public Object createUserForPrivilege(UserCreationDTO userCreation, MultipartFile imgUser) throws Exception {

        String uploadImgUrl = cloudinaryService.uploadFile(imgUser);

        if(userCreation.getUsername().isBlank() || userCreation.getPassword().isBlank() || userCreation.getEmail().isBlank() || userCreation.getName().isBlank() || userCreation.getPhoneNumber().isBlank())
            throw new CustomResponseException(StatusResponseDTO.FIELD_IS_MISSING);

        UserEntity newUser = new UserEntity();
        newUser.setUsername(userCreation.getUsername());
        newUser.setPassword(userCreation.getPassword());
        newUser.setEmail(userCreation.getEmail());
        newUser.setName(userCreation.getName());
        newUser.setPassword(passwordEncoder.encode(userCreation.getPassword()));
        newUser.setImgURL(uploadImgUrl);
        newUser.setEnabled(true);
        newUser.setCreatedTimestamp(new Date());
        newUser.setPhoneNumber(userCreation.getPhoneNumber());
        List<UserRoleEntity> listRole = new LinkedList<>();
        for (UserRoleDTO userRole: userCreation.getRoles()
             ) {
            listRole.add(userRoleServiceImpl.getUserRoleByEnumName(UserRoleEnum.valueOfCode(userRole.getName())));
        }
        newUser.setRoles(listRole);
        return userRepository.save(newUser);

    }

    @Override
    public UserEntity findUserByUserName(String username) {
        return userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND));

    }

    @Override
    public Object sendVerificationEmail(String email) throws IOException {

        if (!StringUtils.hasText(email)) {
            throw new CustomResponseException(StatusResponseDTO.EMAIL_NOT_BLANK);        }

        UserEntity existingUser = userRepository.findByEmail(email).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND));;
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
        UserEntity userExist  = userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND));

        if(!passwordEncoder.matches(newPasswordRequest.getOldPassword(), userExist.getPassword())) throw new RuntimeException("Mật khẩu cũ không đúng !");

        if(!StringUtils.hasText(newPasswordRequest.getNewPassword()) || !StringUtils.hasText(newPasswordRequest.getRetypeNewPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu không được để trống");

        if (!newPasswordRequest.getNewPassword().equals(newPasswordRequest.getRetypeNewPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hai mật khẩu không trùng nhau");
        userExist.setPassword(passwordEncoder.encode(newPasswordRequest.getNewPassword()));
        return userMapper.userToUserDTO(userRepository.save(userExist));

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
        UserEntity user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND));
        return userMapper.userToUserDTO(user);
    }

    @Override
    public Object resetPassword(ResetPasswordDTO payload) throws Exception {
        try {
            ResetPasswordUtils.validConstraint(payload);

            ResetTokenEntity resetToken = resetTokenRepository.findByToken(payload.getToken()).orElseThrow(() -> new RuntimeException("Token is not valid !"));
            if(!payload.getPassword().equals(payload.getRetypePassword())) throw new RuntimeException("Password is not matching");
            UserEntity existingUser = userRepository.findById(resetToken.getUserId().getId()).get();

            existingUser.setPassword(passwordEncoder.encode(payload.getPassword()));

            resetToken.setActive(false);
            resetTokenRepository.save(resetToken);
            return existingUser;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
