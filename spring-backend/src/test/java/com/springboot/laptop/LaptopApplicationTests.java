package com.springboot.laptop;

import com.springboot.laptop.model.*;
import com.springboot.laptop.model.enums.UserRoleEnum;
import com.springboot.laptop.repository.*;
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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	ProductRepository productRepository;



	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	@Autowired
	private UserRepository userRepository;


	@Test
	public void addNewRole() {
		UserRoleEntity userRole = new UserRoleEntity();
		userRole.setName(UserRoleEnum.ROLE_USER.name());
		userRole.setDescription("Khach hang");
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
		appClient.setEnabled(true);
		appClient.setPassword(passwordEncoder.encode("123456"));
		userRepository.save(appClient);
	}

	@Test
	public void getPendingOrders() {
//		Optional<Order> pendingOrders = orderRepository.findById(101L);
//		Long toal = pendingOrders.get().getTotal()
//		System.out.println("Order pending " + pendingOrders);

		Integer order = orderRepository.countNewOrders(1);
		System.out.println("Order is " + order);

	}



	@Test
	public void getNumberOrderRejectedAndCancel() {
//		Optional<Order> pendingOrders = orderRepository.findById(101L);
//		Long toal = pendingOrders.get().getTotal()
//		System.out.println("Order pending " + pendingOrders);

		Integer order = orderRepository.countRejectAndCancelToday();
		System.out.println("Number rejected order is " + order);

	}

	@Test
	public void getTotalRevenueThisWeek() {
		Float money = orderRepository.totalRevenueThisWeek();
		System.out.println("Number rejected order is " + money);

	}

	@Test
	public void getTotalRevenueToday() {
		Float money = orderRepository.totalRevenueToday();
		System.out.println("Number rejected order is " + money);

	}


	@Test
	public void getTopSaleCategory() {
		List<Object[]> result = orderRepository.getCategoryRevenue();
		for (Object[] row : result) {
			Integer id = ((BigInteger) row[0]).intValue();
			String categoryName = (String) row[1];
			Integer total = ((BigInteger) row[2]).intValue();
			// do something with the values
			System.out.println("Value is " + " "+id + " " + categoryName + " " + total);
		}
	}


	@Test
	public void getProductLowerPrice() {


		List<ProductEntity> products = productRepository.getProductsWithMaxPrice(800L);
		for (ProductEntity product: products
			 ) {
			System.out.println(product.getName());
		}

	}

	@Test
	public void getProductHasNameLike() {


		List<ProductEntity> products = productRepository.getProductByName("Laptop");
		for (ProductEntity product: products
		) {
			System.out.println(product.getName());
		}

	}


	@Test
	public void getBestSellerProducts() {


		List<ProductEntity> products = productRepository.findBestSellerProducts();
		for (ProductEntity product: products
		) {
			System.out.println(product.getName());
		}

	}


	@Test
	public void addNewCate() {
		CategoryEntity category = new CategoryEntity();
		category.setEnabled(true);
		category.setName("iphone xsmax");
		categoryRepository.save(category);
	}

}
