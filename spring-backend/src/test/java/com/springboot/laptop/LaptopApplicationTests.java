package com.springboot.laptop;

import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.UserRoleEntity;
import com.springboot.laptop.model.enums.UserRoleEnum;
import com.springboot.laptop.repository.CategoryRepository;
import com.springboot.laptop.repository.UserRepository;
import com.springboot.laptop.repository.UserRoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

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

	@Autowired
	CategoryRepository categoryRepository;



	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	@Autowired
	private UserRepository userRepository;


	@Test
	public void addNewRole() {
		UserRoleEntity userRole = new UserRoleEntity();
		userRole.setName(UserRoleEnum.ROLE_ADMIN.name());
		userRole.setDescription("Quan tri vien");
		userRoleRepository.save(userRole);
	}


//	@Test
//	public void testDecode() {
//		String pass1 = passwordEncoder.encode("123456");
//		String pass2 = passwordEncoder.encode("123456");
//		System.out.println("Pass1 \n" + pass1 + "\n pass2 \n" + pass2);
//
//		System.out.println("IS EQUAL " + passwordEncoder.matches(pass1, pass2));
//	}

	@Test
	public void testDecode() {
		String hashedPasswordFromDatabase = "'$2a$10$lio2nEiKeJjXL.HjoZb7z.u1IkGM6vlpcJS4n9tTSp3679jtiLs6.'";
		String plaintextPassword = "123456";
		boolean passwordMatches = passwordEncoder.matches(plaintextPassword, hashedPasswordFromDatabase);
		if (passwordMatches) {
			System.out.println("Password is correct!");
		} else {
			System.out.println("Password is incorrect!");
		}
	}


	@Test
	public void addNewUser() {
		UserEntity appClient = new UserEntity();
		List<UserRoleEntity> listRoles = new ArrayList<>();
		if (userRoleRepository.findByName("ROLE_ADMIN").isPresent()) {
			UserRoleEntity role = userRoleRepository.findByName("ROLE_ADMIN").get();
			listRoles.add(role);
		}
		appClient.setRoles(listRoles);
		appClient.setUsername("admin");
		appClient.setEmail("admin2001@gmail.com");
		appClient.setPassword(passwordEncoder.encode("123456"));
		userRepository.save(appClient);
	}

	@Test
	public void addNewCate() {
		CategoryEntity category = new CategoryEntity();
		category.setEnabled(true);
		category.setName("iphone xsmax");
		categoryRepository.save(category);
	}

}
