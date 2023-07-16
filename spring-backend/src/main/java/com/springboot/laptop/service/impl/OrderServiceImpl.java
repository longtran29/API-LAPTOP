package com.springboot.laptop.service.impl;

import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.exception.OrderStatusException;
import com.springboot.laptop.mapper.AddressMapper;
import com.springboot.laptop.mapper.OrderDetailsMapper;
import com.springboot.laptop.model.*;
import com.springboot.laptop.model.dto.OrderDetailsDTO;
import com.springboot.laptop.model.dto.ProductDTO;
import com.springboot.laptop.model.dto.UserDTO;
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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;

    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    private final AddressMapper addressMapper;

    private final String ORDER_INFO_TEMPLATE_NAME =  "order_info.ftl";
    private static final String FROM = "BAMBOO STORE<%s>";
    private static final String SUBJECT = "Xác nhận đơn hàng ";

    private final  Configuration configuration;
    private final MailService mailService;
    private final OrderDetailsMapper orderDetailsMapper;


    @Override
    public List<OrderResponseDTO> getOrders() {
        List<Order> orders =  orderRepository.findAll();
        List<OrderResponseDTO> userOrders = new ArrayList<>();

        for(Order order : orders) {
            List<OrderDetails> orderDetails = order.getOrderDetails();
            List<OrderDetailsDTO> orderDetailResponseDTOS = new ArrayList<>();
            for(OrderDetails orderDetails1 : orderDetails) {
                orderDetailResponseDTOS.add(orderDetailsMapper.orderDetailToDTO(orderDetails1));
            }
            userOrders.add(OrderResponseDTO.builder().user(UserDTO.builder().email(order.getUser().getEmail()).username(order.getUser().getUsername()).name(order.getUser().getName()).build()).orderDate(order.getOrderDate()).id(order.getId()).orderDetails(orderDetailResponseDTOS).total(order.getTotal()).status(order.getOrderStatus().name()).statusName(order.getOrderStatus().getName()).build());
        }

        return userOrders;

    }

    @Override
    public Object getOrderDetails(Long orderId) {
        Order order = this.findById(orderId);

        List<OrderDetailsDTO> orderDetails = new ArrayList<>();
        for (OrderDetails orderDetail : order.getOrderDetails()) {
            orderDetails.add(orderDetailsMapper.orderDetailToDTO(orderDetail));
        }

        OrderResponseDTO orderResponse = OrderResponseDTO.builder().id(orderId).user(UserDTO.builder().username(order.getUser().getUsername()).email(order.getUser().getEmail()).build()).orderDate(order.getOrderDate()).status(order.getOrderStatus().name()).statusName(order.getOrderStatus().getName()).total(order.getTotal()).address(addressMapper.addressToDTO(order.getAddress())).build();
        return orderResponse;
    }


    @Override
    public Order cancelOrders(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.ORDER_NOT_FOUND));
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
        Order order = orderRepository.findById(changeStatusDTO.getOrderId()).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.ORDER_NOT_FOUND));
        order.setOrderStatus(status);

        return orderRepository.save(order);

    }


    @Override
    public List<OrderResponseDTO> getUserOrders() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND));

        List<Order> userOrder = user.getOrders();
        List<OrderResponseDTO> userOrders = new ArrayList<>();
        for(Order order : userOrder) {
            List<OrderDetails> orderDetails = order.getOrderDetails();
            List<OrderDetailsDTO> orderDetailResponseDTOS = new ArrayList<>();
            for(OrderDetails orderDetails1 : orderDetails) {
                orderDetailResponseDTOS.add(orderDetailsMapper.orderDetailToDTO(orderDetails1));
            }
            userOrders.add(OrderResponseDTO.builder().orderDate(order.getOrderDate()).id(order.getId()).orderDetails(orderDetailResponseDTOS).total(order.getTotal()).status(order.getOrderStatus().name()).statusName(order.getOrderStatus().getName()).build());
        }

        return userOrders;
    }

    @Override
    public Order findById(Long orderId) {
        orderRepository.findById(orderId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.ORDER_NOT_FOUND));
        return orderRepository.findById(orderId).get();
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

    @Transactional
    @Override
    public Order checkout(OrderRequestDTO orderRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username).orElseThrow(()-> new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND));
        UserCart userCart = user.getCart();
        if(userCart == null) throw new CustomResponseException(StatusResponseDTO.CART_NOT_EXIST);
        List<CartDetails> cartDetailList = userCart.getCartDetails();
        Address userAddress;
        addressRepository.findById(orderRequest.getAddressId()).orElseThrow(()-> new CustomResponseException(StatusResponseDTO.ADDRESS_NOT_FOUND));
        userAddress = addressRepository.findById(orderRequest.getAddressId()).get();

        Order order = new Order();
        order.setAddress(userAddress);
        order.setOrderDate(new Date());
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
