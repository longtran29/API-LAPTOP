package com.springboot.laptop;

import com.springboot.laptop.model.*;
import com.springboot.laptop.model.dto.response.CreatePaymentResponse;
import com.springboot.laptop.model.enums.PaymentMethod;
import com.springboot.laptop.model.enums.UserRoleEnum;
import com.springboot.laptop.repository.*;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.*;

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

	@Autowired
	EntityManager em;




	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	@Autowired
	private UserRepository userRepository;


	@Test
	public void addNewRole() {
		UserRoleEntity userRole = new UserRoleEntity();
		userRole.setName(UserRoleEnum.ROLE_ADMIN);
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


//	@Test
//	public void addNewUser() {
//		UserEntity appClient = new UserEntity();
//		List<UserRoleEntity> listRoles = new ArrayList<>();
//		if (userRoleRepository.findByName(UserRoleEnum.ROLE_ADMIN).isPresent()) {
//			UserRoleEntity role = userRoleRepository.findByName(UserRoleEnum.ROLE_ADMIN).get();
//			listRoles.add(role);
//		}
//		appClient.setRoles(listRoles);
//		appClient.setUsername("admin");
//		appClient.setEmail("admin2001@gmail.com");
//		appClient.setEnabled(true);
//		appClient.setPassword(passwordEncoder.encode("123456"));
//		userRepository.save(appClient);
//	}


//	@Test
//	public void getTotalProducts() {
//
//		List<ProductEntity> listBook = productRepository.findAll();
//
//		Assert.assertEquals(listBook.size(), 4);
//
//	}

//	@Test
//	public void findProductsWithName() {
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<ProductEntity> cq = cb.createQuery(ProductEntity.class);
//
//		Root<ProductEntity> product = cq.from(ProductEntity.class);
//		Predicate price = cb.like(product.get("name"), "Laptop ");
//		cq.where(price);
//	TypedQuery<ProductEntity> query =  em.createQuery(cq);
////		return query.getResultList();
//
//		Assert.assertEquals(query.getResultList().size(), 2);
//
//}


//	@Test
//	public void getPendingOrders() {
////		Optional<Order> pendingOrders = orderRepository.findById(101L);
////		Long toal = pendingOrders.get().getTotal()
////		System.out.println("Order pending " + pendingOrders);
//
//		Integer order = orderRepository.countNewOrders(1);
//		System.out.println("Order is " + order);
//
//	}
//
//
//
//
//	@Test
//	public void  testStrip() throws StripeException {
//
////		stripe js: https://stripe.com/docs/js/including
//
////		tham khao git : https://github.com/oracle-quickstart/oci-caas-pci-ecommerce/blob/497f73428a9912a7aca6dd7513272d2af5a5900e/src/main/java/com/oci/caas/pciecommerce/rest/TestRestController.java#L28
//
//		String public_key = "pk_test_51NEH4zD418vZNZvg2Si23JwSsO7EdrNjIh9JkIfOimNQ4hwn4nhbXuKFLaq8nOjmBns1eWGy7QW3Nnu8iATt7WET000nvU6aAz";
//		String private_key = "sk_test_51NEH4zD418vZNZvgwa8aLlDUWPGw4Uvfpu9WMeDlFCGGxxwKepdDUzwtB3oQA6J6bJwpRr5C5PFdGaBsg8Ky7XWF00h8byWwVM";
//		Stripe.apiKey = private_key ;
//			PaymentIntentCreateParams createParams = new
//					PaymentIntentCreateParams.Builder()
//					.setCurrency("usd")
//					.setAmount(50 * 100L)
//					.build();
//
////		https://stripe.com/docs/api/payment_intents/create
//		// Create a PaymentIntent with the order amount and currency
//		PaymentIntent intent = PaymentIntent.create(createParams);			// cần có Stripe.apiKey đ tạo ra PaymentIntent, Stripe sẽ tự sử dụng apiKey trong process này
//
//		// Send publishable key and PaymentIntent details to client
//		CreatePaymentResponse paymentResponse = new CreatePaymentResponse(public_key, intent.getClientSecret());
////		return paymentResponse;
//		System.out.println("Value paymentresponse " + paymentResponse + " va " + intent.getClientSecret());
//		}
//
//
//		@Test
//		public void getMethodType() {
//			 PaymentMethod paymentMethod= PaymentMethod.getPaymentMethod("Tiền mặt");
//			System.out.println(paymentMethod.name() );
//		}
//
//
//
//		@Test
//	public void getNumberOrderRejectedAndCancel() {
////		Optional<Order> pendingOrders = orderRepository.findById(101L);
////		Long toal = pendingOrders.get().getTotal()
////		System.out.println("Order pending " + pendingOrders);
//
//		Integer order = orderRepository.countRejectAndCancelToday();
//		System.out.println("Number rejected order is " + order);
//
//	}
//
//	@Test
//	public void getTotalRevenueThisWeek() {
//		Float money = orderRepository.totalRevenueThisWeek();
//		System.out.println("Number rejected order is " + money);
//
//	}
//
//	@Test
//	public void getTotalRevenueToday() {
//		Float money = orderRepository.totalRevenueToday();
//		System.out.println("Number rejected order is " + money);
//
//	}
//
//
//	@Test
//	public void getTopSaleCategory() {
//		List<Object[]> result = orderRepository.getCategoryRevenue();
//		for (Object[] row : result) {
//			Integer id = ((BigInteger) row[0]).intValue();
//			String categoryName = (String) row[1];
//			Integer total = ((BigInteger) row[2]).intValue();
//			// do something with the values
//			System.out.println("Value is " + " "+id + " " + categoryName + " " + total);
//		}
//	}
//
//
//	@Test
//	public void getProductLowerPrice() {
//
//
//		List<ProductEntity> products = productRepository.getProductsWithMaxPrice(800L);
//		for (ProductEntity product: products
//			 ) {
//			System.out.println(product.getName());
//		}
//
//	}
//
//	@Test
//	public void getProductHasNameLike() {
//
//
//		List<ProductEntity> products = productRepository.getProductByName("Laptop");
//		for (ProductEntity product: products
//		) {
//			System.out.println(product.getName());
//		}
//
//	}
//
//
//	@Test
//	public void getBestSellerProducts() {
//
//
//		List<ProductEntity> products = productRepository.findBestSellerProducts();
//		for (ProductEntity product: products
//		) {
//			System.out.println(product.getName());
//		}
//
//	}
//
//
	@Test
	public void addNewCate() {
		CategoryEntity category = new CategoryEntity();
		category.setEnabled(true);
		category.setName("iphone xsmax");
		categoryRepository.save(category);
	}

}
