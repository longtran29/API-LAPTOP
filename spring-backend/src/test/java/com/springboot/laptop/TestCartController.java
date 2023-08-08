package com.springboot.laptop;

import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.UserRoleEntity;
import com.springboot.laptop.model.enums.UserRoleEnum;
import com.springboot.laptop.repository.UserRepository;
import com.springboot.laptop.repository.UserRoleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
@Transactional
public class TestCartController {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
            private UserRoleRepository userRoleRepository;



    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//    @Test
//    void addToCart() {
//
//        UserEntity existingUser = userRepository.findByUsernameIgnoreCase("admin").get();
//
////        existingUser.setPassword(passwordEncoder.encode("123456"));
//        List<UserRoleEntity> listRoles = new ArrayList<>();
//        if (userRoleRepository.findByName(UserRoleEnum.ROLE_ADMIN).isPresent()) {
//            UserRoleEntity role = userRoleRepository.findByName(UserRoleEnum.ROLE_ADMIN).get();
//            listRoles.add(role);
//        }
//        existingUser.setRoles(listRoles);
//        userRepository.save(existingUser);
//
//
//    }

    @Test
    void testUpdatePass() {

                UserEntity existingUser = userRepository.findByUsernameIgnoreCase("admin").get();



                boolean checkDuplicate = passwordEncoder.matches("1234567", existingUser.getPassword());
                Assertions.assertThat(checkDuplicate).isTrue();



    }

}
