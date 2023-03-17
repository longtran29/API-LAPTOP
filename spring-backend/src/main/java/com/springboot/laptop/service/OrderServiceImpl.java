package com.springboot.laptop.service;

import com.springboot.laptop.model.*;
import com.springboot.laptop.model.dto.request.OrderRequestDTO;
import com.springboot.laptop.model.enums.OrderStatus;
import com.springboot.laptop.repository.AddressRepository;
import com.springboot.laptop.repository.CartRepository;
import com.springboot.laptop.repository.OrderRepository;
import com.springboot.laptop.repository.UserRepository;
import com.springboot.laptop.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;

    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    @Autowired
    public OrderServiceImpl(UserRepository userRepository, AddressRepository addressRepository, OrderRepository orderRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;

    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }


    public Order checkout(OrderRequestDTO orderRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username).get();


        UserCart userCart = user.getCart();
        List<CartDetails> cartDetailList = userCart.getCartDetails();

        Address userAddress = addressRepository.findById(orderRequest.getAddressId()).get();



        Order order = new Order();
        order.setAddress(userAddress);
        order.setOrderDate(LocalDateTime.now());
        order.setUser(user);
        order.setOrderStatus(OrderStatus.NEW);

        // add order detail to the order
        List<OrderDetails> orderDetailList = new ArrayList<>();
        float total = 0;
        for(CartDetails cartDetail : cartDetailList) {
            ProductEntity product = cartDetail.getProduct();

            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrder(order);
            orderDetails.setProduct(product);
            orderDetails.setQuantity(cartDetail.getQuantity());
            orderDetails.setTotal((cartDetail.getProduct().getOriginal_price()* cartDetail.getProduct().getDiscount_percent()) * cartDetail.getQuantity() );


            orderDetailList.add(orderDetails);
            total += orderDetails.getTotal();
        }

        order.setTotal(total);
        order.setOrderDetails(orderDetailList);
        cartRepository.deleteById(user.getCart().getId());

        return orderRepository.save(order);
    }
}
