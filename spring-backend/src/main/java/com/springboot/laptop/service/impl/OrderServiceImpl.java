package com.springboot.laptop.service.impl;

import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.exception.OrderStatusException;
import com.springboot.laptop.model.*;
import com.springboot.laptop.model.dto.request.ChangeStatusDTO;
import com.springboot.laptop.model.dto.request.OrderInfoMail;
import com.springboot.laptop.model.dto.request.OrderRequestDTO;
import com.springboot.laptop.model.dto.response.*;
import com.springboot.laptop.model.enums.OrderStatus;
import com.springboot.laptop.model.enums.PaymentMethod;
import com.springboot.laptop.repository.AddressRepository;
import com.springboot.laptop.repository.CartRepository;
import com.springboot.laptop.repository.OrderRepository;
import com.springboot.laptop.repository.UserRepository;
import com.springboot.laptop.service.MailService;
import com.springboot.laptop.service.OrderService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;

    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    private final String ORDER_INFO_TEMPLATE_NAME =  "order_info.ftl";
    private static final String FROM = "BAMBOO STORE<%s>";
    private static final String SUBJECT = "Xác nhận đơn hàng ";

    @Autowired private Configuration configuration;
    @Autowired private MailService mailService;
    @Autowired
    public OrderServiceImpl(UserRepository userRepository, AddressRepository addressRepository, OrderRepository orderRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;

    }


    @Override
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

    @Override
    public Object getOrderDetails(Long orderId) {
        Order order = this.findById(orderId);

        List<OrderDetailResponseDTO> orderDetails = new ArrayList<>();
        for (OrderDetails orderDetail : order.getOrderDetails()) {
            orderDetails.add(OrderDetailResponseDTO.builder().product(ProductResponseDTO.builder().id(orderDetail.getProduct().getId()).primaryImage(orderDetail.getProduct().getPrimaryImage()).name(orderDetail.getProduct().getName()).build()).quantity(orderDetail.getQuantity()).total(orderDetail.getTotal()).build());
        }

        OrderResponseDTO orderResponse = OrderResponseDTO.builder().id(orderId).user(UserResponseDTO.builder().username(order.getUser().getUsername()).email(order.getUser().getEmail()).build()).orderDate(order.getOrderDate()).status(order.getOrderStatus().name()).statusName(order.getOrderStatus().getName()).total(order.getTotal()).address(AddressResponseDTO.builder().address(order.getAddress().getAddress()).city(order.getAddress().getCity()).phoneNumber(order.getAddress().getPhoneNumber()).build()).orderDetails(orderDetails).build();
        return orderResponse;
    }


    @Override
    public Order cancelOrders(Long orderId) {
        Order order = orderRepository.findById(orderId).get();
        if(order == null) throw new CustomResponseException(StatusResponseDTO.ORDER_NOT_FOUND);
        if(order.getOrderStatus().equals(OrderStatus.SHIPPED)) {
            throw new CustomResponseException(StatusResponseDTO.ORDER_CANCEL_VIOLATION);
        }
        if(order.getOrderStatus().equals(OrderStatus.REJECTED)) throw new CustomResponseException(StatusResponseDTO.ORDER_REJECTED_VIOLATION);
        if(order.getOrderStatus().equals(OrderStatus.CANCELED)) throw new CustomResponseException(StatusResponseDTO.ORDER_CANCELED_VIOLATION);
        if(order.getOrderStatus().equals(OrderStatus.DELIVERED)) throw new CustomResponseException(StatusResponseDTO.ORDER_DELIVERED_VIOLATION);
        if(order.getOrderStatus().equals(OrderStatus.SHIPPED)) throw new CustomResponseException(StatusResponseDTO.ORDER_SHIPPED_VIOLATION);

        order.setOrderStatus(OrderStatus.CANCELED);
        return orderRepository.save(order);
    }

    @Override
    public Order changeStatus(ChangeStatusDTO changeStatusDTO) {
        OrderStatus status = OrderStatus.getStatus(changeStatusDTO.getStatusName());

        if(!orderRepository.findById(changeStatusDTO.getOrderId()).isPresent()) {
            throw new CustomResponseException(StatusResponseDTO.ORDER_NOT_FOUND);
        } else {
            Order order = orderRepository.findById(changeStatusDTO.getOrderId()).get();
            order.setOrderStatus(status);

            return orderRepository.save(order);
        }

    }


    @Override
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

    @Override
    public Order findById(Long orderId) {
        if(!orderRepository.findById(orderId).isPresent()) {
            throw new CustomResponseException(StatusResponseDTO.ORDER_NOT_FOUND);
        } else {
            return orderRepository.findById(orderId).get();
        }
    }

    @Override
    public Object sendMail4Order(OrderCompleted order) {

        Map orderInfo = orderInfo(order);
        try {
            Template template = configuration.getTemplate(ORDER_INFO_TEMPLATE_NAME);
            OrderInfoMail orderIn4 = OrderInfoMail.builder()
                    .from(String.format(FROM, "tuna.music@gmail.com"))
                    .to(order.getEmail())
                    .text(FreeMarkerTemplateUtils.processTemplateIntoString(template, orderInfo))
                    .subject(SUBJECT +  " từ BAMBOO STORE")
                    .build();
            mailService.send4OrderInfo(orderIn4).get();
            return order;
        } catch (Exception e) {
            throw new CustomResponseException(StatusResponseDTO.INTERNAL_SERVER);
        }
    }


    public Map orderInfo(OrderCompleted order) {
        Map<String, Object> map = new HashMap<>();

        map.put("ADDRESS", order.getDeliveryAddress().getAddress() + " " + order.getDeliveryAddress().getCity() + " " + order.getDeliveryAddress().getCountry() + " " + order.getDeliveryAddress().getZipcode());
        map.put("TOTAL_AMOUT", order.getTotalAmt() );
        map.put("TOTAL_PRICE", order.getCartTotal() );
        map.put("PRODUCT", order.getOrderedProducts());
        map.put("PHONENUMBER", order.getDeliveryAddress().getPhoneNumber());
        map.put("EMAIL", order.getEmail());

        return map;
    }

    @Override
    public Order checkout(OrderRequestDTO orderRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username).get();
        UserCart userCart = user.getCart();
        if(userCart == null) throw new CustomResponseException(StatusResponseDTO.CART_NOT_EXIST);
        List<CartDetails> cartDetailList = userCart.getCartDetails();
        Address userAddress;
        if(addressRepository.findById(orderRequest.getAddressId()).isEmpty()) {
            throw new CustomResponseException(StatusResponseDTO.ADDRESS_NOT_FOUND);
        }
        userAddress = addressRepository.findById(orderRequest.getAddressId()).get();

        Order order = new Order();
        order.setAddress(userAddress);
        order.setOrderDate(LocalDateTime.now());
        order.setUser(user);
        order.setOrderStatus(OrderStatus.NEW);
        order.setMethodPayment(PaymentMethod.getPaymentMethod(orderRequest.getMethodPayment()));

        // add order detail to the order
        List<OrderDetails> orderDetailList = new ArrayList<>();
        float total = 0L;
        for(CartDetails cartDetail : cartDetailList) {
            ProductEntity product = cartDetail.getProduct();

            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrder(order);
            orderDetails.setProduct(product);
            orderDetails.setQuantity(cartDetail.getQuantity());
            orderDetails.setTotal((cartDetail.getProduct().getOriginal_price() - ((cartDetail.getProduct().getOriginal_price()* cartDetail.getProduct().getDiscount_percent()))) * cartDetail.getQuantity() );


            orderDetailList.add(orderDetails);
            total += orderDetails.getTotal();
        }

        order.setTotal(total);
        order.setOrderDetails(orderDetailList);
        cartRepository.deleteById(user.getCart().getId());

        return orderRepository.save(order);
    }
}
