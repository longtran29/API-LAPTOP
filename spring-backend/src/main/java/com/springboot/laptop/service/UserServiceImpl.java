package com.springboot.laptop.service;

import com.springboot.laptop.model.Address;
import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.UserRoleEntity;
import com.springboot.laptop.model.dto.request.AddressRequestDTO;
import com.springboot.laptop.model.dto.request.AppClientSignUpDTO;
import com.springboot.laptop.model.enums.UserRoleEnum;
import com.springboot.laptop.repository.UserRepository;
import com.springboot.laptop.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Value("${spring.mail.username}")
    private String emailown;

    @Autowired
    private JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleServiceImpl userRoleServiceImpl;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserRoleServiceImpl userRoleServiceImpl) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleServiceImpl = userRoleServiceImpl;

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
    public void sendVerificationEmail(String email, String siteURL) throws UnsupportedEncodingException, MessagingException, MessagingException, UnsupportedEncodingException {
        String toAddress = email;
        String fromAddress = emailown;
        String senderName = "Booking";
        String subject = "Chỉ cần nhấp chuột để xác nhận";
        String content = "<h1>Xác minh địa chỉ email của bạn</h1>" +
                "Bạn vừa tạo tài khoản với địa chỉ email: [[email]]<br>" +
                "Nhấn nút \"Xác nhận\" để chứng thực địa chỉ email và mở khóa cho toàn bộ tài khoản.<br>" +
                "Chúng tôi cũng sẽ nhập các đặt phòng bạn đã thực hiện với địa chỉ email này.<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">Xác nhận</a></h3>";
        content = content.replace("[[email]]", email);
        System.out.println("verifyURL = " + siteURL);
        content = content.replace("[[URL]]", siteURL);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    @Override
    public UserEntity newPassword(String email, String password, String passwordConfirm) {
        UserEntity userExist= userRepository.findByEmail(email).get();
        if (userExist != null && StringUtils.hasText(passwordConfirm) && StringUtils.hasText(password)) {
            if (password.equals(passwordConfirm)) {
                userExist.setPassword(passwordEncoder.encode(password));
                userRepository.save(userExist);
                System.out.println("newPassord thành công");
                return userExist;
            }
            else{
                System.out.println("Hai password không giống nhau");
                return null;
            }
        }
        else{
            System.out.println("newPassword thất bại");
            return null;
        }
    }

}
