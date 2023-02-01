package com.springboot.laptop;

import com.springboot.laptop.model.UserRoleEntity;
import com.springboot.laptop.model.enums.UserRoleEnum;
import com.springboot.laptop.repository.UserRoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
@Transactional
class LaptopApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	UserRoleRepository userRoleRepository;

	@Test
	public void addNewRole() {
		UserRoleEntity userRole = new UserRoleEntity();
		userRole.setName(UserRoleEnum.ROLE_USER.name());
		userRole.setDescription("Người dùng");
		userRoleRepository.save(userRole);
	}


}
