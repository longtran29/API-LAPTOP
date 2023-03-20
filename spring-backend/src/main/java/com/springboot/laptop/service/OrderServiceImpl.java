package com.springboot.laptop.service;

import com.springboot.laptop.model.*;
import com.springboot.laptop.model.dto.request.ChangeStatusDTO;
import com.springboot.laptop.model.dto.request.OrderRequestDTO;
import com.springboot.laptop.model.dto.response.OrderDetailResponseDTO;
import com.springboot.laptop.model.dto.response.OrderResponseDTO;
import com.springboot.laptop.model.dto.response.ProductResponseDTO;
import com.springboot.laptop.model.dto.response.UserResponseDTO;
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

    public List<OrderResponseDTO> getOrders() {
        List<Order> orders =  orderRepository.findAll();
        List<OrderResponseDTO> userOrders = new ArrayList<>();

        for(Order order : orders) {
            List<OrderDetails> orderDetails = order.getOrderDetails();
            List<OrderDetailResponseDTO> orderDetailResponseDTOS = new ArrayList<>();
            for(OrderDetails orderDetails1 : orderDetails) {
                orderDetailResponseDTOS.add(OrderDetailResponseDTO.builder().product(ProductResponseDTO.builder().primaryImage(orderDetails1.getProduct().getPrimaryImage()).name(orderDetails1.getProduct().getName()).build()).total(orderDetails1.getTotal()).quantity(orderDetails1.getQuantity()).build());
            }
            userOrders.add(OrderResponseDTO.builder().user(UserResponseDTO.builder().email(order.getUser().getEmail()).username(order.getUser().getUsername()).name(order.getUser().getName()).build()).orderDate(order.getOrderDate()).id(order.getId()).orderDetails(orderDetailResponseDTOS).total(order.getTotal()).status(order.getOrderStatus().name()).statusName(order.getOrderStatus().getName()).build());
        }

        return userOrders;

    }

    public Order changeStatus(ChangeStatusDTO changeStatusDTO) {
        OrderStatus status = OrderStatus.getStatus(changeStatusDTO.getStatusName());

        Order order = orderRepository.findById(changeStatusDTO.getOrderId()).get();
        order.setOrderStatus(status);

        return orderRepository.save(order);
    }

    public List<OrderResponseDTO> getUserOrders() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username).get();

        List<Order> userOrder = user.getOrders();
        List<OrderResponseDTO> userOrders = new ArrayList<>();
        for(Order order : userOrder) {
            List<OrderDetails> orderDetails = order.getOrderDetails();
            List<OrderDetailResponseDTO> orderDetailResponseDTOS = new ArrayList<>();
            for(OrderDetails orderDetails1 : orderDetails) {
                orderDetailResponseDTOS.add(OrderDetailResponseDTO.builder().product(ProductResponseDTO.builder().primaryImage(orderDetails1.getProduct().getPrimaryImage()).name(orderDetails1.getProduct().getName()).build()).total(orderDetails1.getTotal()).quantity(orderDetails1.getQuantity()).build());
            }
            userOrders.add(OrderResponseDTO.builder().orderDate(order.getOrderDate()).id(order.getId()).orderDetails(orderDetailResponseDTOS).total(order.getTotal()).status(order.getOrderStatus().name()).statusName(order.getOrderStatus().getName()).build());
        }

        return userOrders;
    }
    public Order findById(Long orderId) {
        return orderRepository.findById(orderId).get();
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
