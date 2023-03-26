package com.springboot.laptop.service;

import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.exception.UserPasswordException;
import com.springboot.laptop.model.Address;
import com.springboot.laptop.model.ResetTokenEntity;
import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.UserRoleEntity;
import com.springboot.laptop.model.dto.request.*;
import com.springboot.laptop.model.dto.response.ResponseDTO;
import com.springboot.laptop.model.dto.response.StatusResponseDTO;
import com.springboot.laptop.model.enums.UserRoleEnum;
import com.springboot.laptop.repository.ResetTokenRepository;
import com.springboot.laptop.repository.UserRepository;
import com.springboot.laptop.service.impl.MailService;
import com.springboot.laptop.service.impl.UserService;
import com.springboot.laptop.utils.UidUtils;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Value("${spring.mail.username}")
    private String emailown;

//    @Autowired
//    private final Configuration configuration;

    public static final String F_DDMMYYYYHHMM = "dd/MM/yyyy HH:mm";


    public static String toStr(Date date, String format) {
        return date != null ? DateFormatUtils.format(date, format) : "";
    }


    private static final String RESET_PASSWORD_TEMPLATE_NAME = "reset_password.ftl";
    private static final String FROM = "Laptop Store<%s>";
    private static final String SUBJECT = "QUÊN MẬT KHẨU";
    private static final String DOMAIN_CLIENT = "http://localhost:8080/reset-password";
    private static final long MINUS_TO_EXPIRED = 10;

    @Autowired
    private JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleServiceImpl userRoleServiceImpl;
    private final ResetTokenRepository resetTokenRepository;
    private final MailService mailService;


    @Autowired
    public UserServiceImpl( UserRepository userRepository, PasswordEncoder passwordEncoder, UserRoleServiceImpl userRoleServiceImpl, ResetTokenRepository resetTokenRepository, MailService mailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleServiceImpl = userRoleServiceImpl;
        this.resetTokenRepository = resetTokenRepository;
        this.mailService = mailService;
//        this.configuration = configuration;

    }

    @Override
    public UserEntity register(AppClientSignUpDTO user) throws Exception {
        UserRoleEntity userRole = this.userRoleServiceImpl.getUserRoleByEnumName(UserRoleEnum.ROLE_USER.name());
        UserEntity appClient = new UserEntity();
        appClient.setRoles(List.of(userRole));
        appClient.setUsername(user.getUsername());
        appClient.setEmail(user.getEmail());
        appClient.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return userRepository.save(appClient);
    }

    @Override
    public boolean userExists(String username, String email) {
        Optional<UserEntity> byUsername = this.userRepository.findByUsername(username);
        Optional<UserEntity> byEmail = this.userRepository.findByEmail(email);
        return byUsername.isPresent() || byEmail.isPresent();
    }

    @Override
    public UserEntity updateInformation(UserRequestDTO userRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username).get();
        if(!userRequest.getName().isEmpty())  {
            user.setName(userRequest.getName());
        }
        if(!userRequest.getPhoneNumber().isEmpty()) {

            user.setPhoneNumber(userRequest.getPhoneNumber());
        }
        if(!userRequest.getImgURL().isEmpty())
        {
            user.setImgURL(userRequest.getImgURL());
        }
        return userRepository.save(user);
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

//    @Override
//    public void sendVerificationEmail(String email, String siteURL) throws Exception {
//
//        Optional<UserEntity> existingUser = userRepository.findByEmail(email);
//
//        if(existingUser.isPresent()) {
//            String toAddress = email;
//            String fromAddress = emailown;
//            String senderName = "Quản trị viên bamboo";
//            String subject = "Chỉ cần nhấp chuột để xác nhận";
//            String content = "<h1>Xác minh địa chỉ email của bạn</h1>" +
//                    "Bạn vừa  chọn quên mật khẩu với địa chỉ email: [[email]]<br>" +
//                    "Nhấn nút \"Xác nhận\" để chứng thực địa chỉ email và đặt mật khẩu mới.<br>"
//                    + "<h3><a href=\"[[URL]]\" target=\"_self\">Xác nhận</a></h3>";
//            content = content.replace("[[email]]", email);
//            System.out.println("verifyURL = " + siteURL);
//            content = content.replace("[[URL]]", siteURL);
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message);
//            helper.setFrom(fromAddress, senderName);
//            helper.setTo(toAddress);
//            helper.setSubject(subject);
//            helper.setText(content, true);
//            mailSender.send(message);
//        } else {
//            throw new NotFoundException("Không tồn tại người dùng với mail này !");
//        }
//
//
//    }

//    @Override
//    public ResponseDTO sendVerificationEmail(String email) throws IOException {
//        ResponseDTO response = new ResponseDTO();
//        UserEntity existingUser;
//        if(!StringUtils.hasText(email)) {
//            throw new CustomResponseException(StatusResponseDTO.EMAIL_NOT_BLANK);
//        }
//
//        if(userRepository.findByEmail(email) != null) {
//            existingUser = userRepository.findByEmail(email).get();
//        } else {
//            throw new CustomResponseException(StatusResponseDTO.ERROR_NOT_FOUND);
//        }
//
//        String token = UidUtils.generateRandomString(9);
//
//        ResetTokenEntity resetToken = ResetTokenEntity.builder().token(token).userId(existingUser).active(true).createdDate(new Date()).build();
//
//        resetTokenRepository.save(resetToken);
//
//
//        freemarker.template.Template template = configuration.getTemplate(RESET_PASSWORD_TEMPLATE_NAME);
//
//        Map model = buildModel(existingUser, resetToken);
//        try {
//            ResetPasswordMailDTO rpmd = ResetPasswordMailDTO.builder().from(String.format(FROM, email))
//                    .to(existingUser.getEmail())
//                    .text(FreeMarkerTemplateUtils.processTemplateIntoString(template, model))
//                    .subject(SUBJECT).build();
//        mailService.sendWForgotPassword(rpmd).get();
//            response.setData("mail send to : " + existingUser.getEmail());
////            response.setSuccessCode(Boolean.TRUE);
//        } catch (Exception e) {
//            throw new CustomResponseException(StatusResponseDTO.INTERNAL_SERVER);
//        }
//
//        return response;
//    }

    private static Map buildModel(UserEntity customer, ResetTokenEntity resetToken) {
        Map<String, Object> model = new HashMap<>();
        model.put("time", toStr(new Date(), F_DDMMYYYYHHMM));
        model.put("name", customer.getName());
        model.put("email", customer.getEmail());
        model.put("link", String.format(DOMAIN_CLIENT, resetToken.getToken()));
        model.put("minusToExpired", MINUS_TO_EXPIRED);
        return model;
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
            }
            else{
                throw new UserPasswordException("Hai mật khẩu không trùng nhau");
            }
        }
        else{
            throw new UserPasswordException("Không được để trống");
        }
    }

}