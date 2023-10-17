package com.springboot.laptop.model.enums;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRoleEnumTest {


    @Test
    void checkROleExist() {
        Assertions.assertThat(UserRoleEnum.valueOf("ROLE_USER")).isNotNull();
    }

}