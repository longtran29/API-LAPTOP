package com.springboot.laptop;

import com.springboot.laptop.config.VnPayConfig;
import com.springboot.laptop.model.*;
import com.springboot.laptop.model.dto.response.CreatePaymentResponse;
import com.springboot.laptop.model.enums.PaymentMethod;
import com.springboot.laptop.model.enums.UserRoleEnum;
import com.springboot.laptop.repository.*;
import com.springboot.laptop.service.AmazonS3Service;
import com.springboot.laptop.service.CategoryService;
import com.springboot.laptop.service.ImportService;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.PaymentIntent;
import net.sf.jasperreports.engine.JRException;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import com.stripe.param.PaymentIntentCreateParams;

import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Transactional
@SpringBootTest
@Rollback(false)
class LaptopApplicationTests {

//	@Test
//	void contextLoads() {
//	}

//	@Autowired
//	private TestEntityManager entityManager;

	@Autowired
	UserRoleRepository userRoleRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
			AccountRepository accountRepository;


	@Autowired
	ImportService importService;

	@Autowired
			CustomerRepository customerRepository;

	@Autowired
	private VnPayConfig vnPayConfig;

	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	@Autowired
	private CustomerRepository userRepository;
	@Autowired
	private AccountRepository employeeRepository;

    @Autowired
    private AmazonS3Service  amazonS3Service;

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ImportDetailRepository importRepository;

	@Test
	public void addNewRole() {
		UserRoleEntity userRole = new UserRoleEntity();
		userRole.setName(UserRoleEnum.ROLE_ADMIN);
		userRole.setDescription("Quan tri vien");
		userRoleRepository.save(userRole);
	}

	@Test
	public void addNewRole1() {
		UserRoleEntity userRole = new UserRoleEntity();
		userRole.setName(UserRoleEnum.ROLE_EMPLOYEE);
		userRole.setDescription("Nhan vien");
		userRoleRepository.save(userRole);
	}


	@Test
	public void addNewRole2() {
		UserRoleEntity userRole = new UserRoleEntity();
		userRole.setName(UserRoleEnum.ROLE_CUSTOMER);
		userRole.setDescription("Khach hang");
		userRoleRepository.save(userRole);
	}

//
//	@Test
//	public void testDecode() {
//		String hashedPasswordFromDatabase = "'$2a$10$lio2nEiKeJjXL.HjoZb7z.u1IkGM6vlpcJS4n9tTSp3679jtiLs6.'";
//		String plaintextPassword = "123456";
//		boolean passwordMatches = passwordEncoder.matches(plaintextPassword, hashedPasswordFromDatabase);
//		if (passwordMatches) {
//			System.out.println("Password is correct!");
//		} else {
//			System.out.println("Password is incorrect!");
//		}
//	}
//

	@Test
	public void addNewUser() {
		Account appClient = new Account();
		List<UserRoleEntity> listRoles = new ArrayList<>();
		if (userRoleRepository.findByName(UserRoleEnum.ROLE_ADMIN).isPresent()) {
			UserRoleEntity role = userRoleRepository.findByName(UserRoleEnum.ROLE_ADMIN).get();
			listRoles.add(role);
		}
		appClient.setRoles(listRoles);
		appClient.setUsername("admin");
		appClient.setEmail("admin2001@gmail.com");
		appClient.setEnabled(true);
		appClient.setPassword(passwordEncoder.encode("123456"));
		accountRepository.save(appClient);
	}


	@Test
	public void addNewCustomer() {
		Customer appClient = new Customer();
		List<UserRoleEntity> listRoles = new ArrayList<>();
		if (userRoleRepository.findByName(UserRoleEnum.ROLE_CUSTOMER).isPresent()) {
			UserRoleEntity role = userRoleRepository.findByName(UserRoleEnum.ROLE_CUSTOMER).get();
			listRoles.add(role);
		}
		appClient.setName("long2001 tran");
		appClient.setUsername("customer");
		appClient.setEmail("customer2001@gmail.com");
		appClient.setEnabled(true);
		appClient.setPassword(passwordEncoder.encode("123456"));
		customerRepository.save(appClient);
	}

	@Test
	public void testExportReport() throws JRException {
		importService.exportReport();
	}

	@Test
	public void lengthImport() throws JRException {
		importRepository.getAllImports();
		Assertions.assertEquals(importRepository.getAllImports().size(), 3);
	}


//	@Test
//	public void getTimeGen() {
//		Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//		String vnp_CreateDate = formatter.format(cld.getTime());
//		System.out.println("Value is "   + vnp_CreateDate);
//
//		String vnp_SecureHash = vnPayConfig.hmacSHA512(vnPayConfig.vnp_HashSecret, hashData.toString());
//	}

//	@Test
//	public void addNewEmployee() {
//		Customer appClient = new Customer();
//
//		appClient.setUsername("employee1");
//		appClient.setEmail("employee2001@gmail.com");
//		appClient.setEnabled(true);
//		appClient.setPassword(passwordEncoder.encode("123456"));
//
////		appClient.setRoles(List.of(userRoleRepository.findByName(UserRoleEnum.ROLE_EMPLOYEE).get()));
//		customerRepository.save(appClient);
//	}


//	@Test
//	public void addNewUser3() {
//		Customer appClient = new Customer();
//		List<UserRoleEntity> listRoles = new ArrayList<>();
//		if (userRoleRepository.findByName(UserRoleEnum.ROLE_ADMIN).isPresent()) {
//			UserRoleEntity role = userRoleRepository.findByName(UserRoleEnum.ROLE_ADMIN).get();
//			listRoles.add(role);
//		}
//		appClient.setRoles(listRoles);
//		appClient.setUsername("long2");
//		appClient.setEmail("long2003@gmail.com");
//		appClient.setEnabled(true);
//		appClient.setPassword(passwordEncoder.encode("123456"));
//		userRepository.save(appClient);
//	}

//	@Test
//	public void whenNullName_thenOneConstraintViolation() {
//		UserEntity user = new UserEntity(null);
//		Set<ConstraintViolation<UserNotNull>> violations = validator.validate(user);
//
//		assertThat(violations.size()).isEqualTo(1);
//	}

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


//    @Test
//    public void getObjectUrl() {
//        System.out.println("Value object is " + amazonS3Service.getObjectUrl());;
//    }


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

//	@Test
//	public void getTotalRevenueToday() {
//		Float money = orderRepository.totalRevenueToday();
//		System.out.println("Number rejected order is " + money);
//
//	}

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
//	@Test
//	public void addNewCate() {
//		CategoryEntity category = new CategoryEntity();
//		category.setEnabled(true);
//		category.setName("iphone xsmax");
//		categoryRepository.save(category);
//	}

}
