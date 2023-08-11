//package com.springboot.laptop.repository;
//
//import com.springboot.laptop.model.Order;
//import com.springboot.laptop.model.OrderDetails;
//import com.springboot.laptop.model.UserEntity;
//import org.assertj.core.api.Assertions;
//import org.hamcrest.MatcherAssert;
//import org.hamcrest.Matchers;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static org.hamcrest.CoreMatchers.*;
//import static org.junit.Assert.assertThat;
//
//
//
//@DataJpaTest
//
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class OrderRepositoryTest {
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired private UserRepository userRepository;
//
////    @Test
////    void countNewOrders() {
////        Assertions.assertThat(orderRepository.countOrderBasedStatus(OrderStatus.CANCELED.toString())).isEqualTo(2);
////
////    }
//
//    @Test
//    void countOrderBasedStatus() {
//    }
//
////    @Test
////    void countRejectAndCancelToday() {
////
////        Assertions.assertThat(orderRepository.countCancelToday(OrderStatus.CANCELED.toString())).isEqualTo(1);
////    }
//
//    @Test
//    void totalRevenueThisWeek() {
//
//        Assertions.assertThat(orderRepository.sumTotalForDeliveredOrdersThisWeek()).isNotZero();
//    }
//
//    @Test
//    void totalRevenueToday() {
//    }
//
//    @Test
//    void getCategoryRevenue() {
//    }
//
//    @Test
//    void findByUser() {
//
//        UserEntity user = userRepository.findByUsernameIgnoreCase("admin").get();
////        Assertions.assertThat((user)).isEqualTo(4);
//
//        Assertions.assertThat(orderRepository.findByUser(user).size()).isEqualTo(4);
//
////        List<Order> userOrders = orderRepository.findByUser(user);
////
////        for (Order order: userOrders
////             ) {
////
////            List<OrderDetails> listDetails = order.getOrderDetails();
////            for (OrderDetails detail: listDetails
////                 ) {
////
////                System.out.println("Value naem prroduct " + detail.getProduct().getName());
////
////            }
////        }
//
//
//
////        orderRepository.findByUser(user);
////
////        Order existingOrder = new Order();
////        existingOrder.setId("O_20230807_124558");
////
////        MatcherAssert.assertThat(orderRepository.findByUser(user), Matchers.hasItems(existingOrder));
//
//
//
////        Assertions.assertThat(orderRepository.findByUser(user).size()).isNotZero();
//
//    }
//}