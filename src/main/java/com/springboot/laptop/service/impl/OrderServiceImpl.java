package com.springboot.laptop.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.laptop.config.PaypalConfig;
import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.mapper.OrderMapper;
import com.springboot.laptop.model.*;
import com.springboot.laptop.model.dto.*;
import com.springboot.laptop.model.dto.request.ChangeStatusDTO;
import com.springboot.laptop.model.dto.request.OrderInfoMail;
import com.springboot.laptop.model.dto.request.OrderRequestDTO;
import com.springboot.laptop.model.dto.request.PaypalCheckoutDTO;
import com.springboot.laptop.model.dto.response.*;
import com.springboot.laptop.model.enums.OrderStatus;
import com.springboot.laptop.model.enums.PaymentMethod;
import com.springboot.laptop.model.enums.PaymentStatus;
import com.springboot.laptop.repository.*;
import com.springboot.laptop.service.MailService;
import com.springboot.laptop.service.OrderService;
import com.springboot.laptop.utils.PaypalUtility;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.springboot.laptop.model.dto.PayPalEndpoints.ORDER_CHECKOUT;
import static com.springboot.laptop.model.dto.PayPalEndpoints.createUrl;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CustomerRepository userRepository;

    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    private final String ORDER_INFO_TEMPLATE_NAME =  "order_info.ftl";
    private static final String FROM = "BAMBOO STORE<%s>";
    private static final String SUBJECT = "Xác nhận đơn hàng ";

    private final  Configuration configuration;
    private final MailService mailService;

    private final OrderMapper orderMapper;

    private final PaypalUtility paypalUtility;

    private final PaypalConfig paypalConfig;

    private final ObjectMapper objectMapper;

    private final HttpClient httpClient =  HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();;

    private final AccountRepository accountRepository;


    @Override
    public List<OrderResponseDTO> getOrders() {
        List<Order> orders =  orderRepository.findAll();
        List<OrderResponseDTO> userOrders = new ArrayList<>();

        orders.forEach(order -> {
            userOrders.add(orderMapper.orderToDTO(order));
        });
        return userOrders;
    }

    @Override
    public Object getOrderDetails(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        return order;
        // return orderMapper.orderToDTO(order);
    }
    @Override
    public Object cancelOrders(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.ORDER_NOT_FOUND));
        if(order.getOrderStatus().equals(OrderStatus.SHIPPED) || order.getOrderStatus().equals(OrderStatus.ON_THE_WAY)) {
            throw new CustomResponseException(StatusResponseDTO.ORDER_CANCEL_VIOLATION);
        }
        if(order.getOrderStatus().equals(OrderStatus.CANCELED)) throw new CustomResponseException(StatusResponseDTO.ORDER_CANCELED_VIOLATION);
        if(order.getOrderStatus().equals(OrderStatus.DELIVERED)) throw new CustomResponseException(StatusResponseDTO.ORDER_DELIVERED_VIOLATION);
        order.setOrderStatus(OrderStatus.CANCELED);
        return orderMapper.orderToDTO(orderRepository.save(order));
    }

    @Override
    public OrderResponseDTO changeStatus(ChangeStatusDTO changeStatusDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND));
        try {
        OrderStatus status = OrderStatus.valueOfCode(changeStatusDTO.getStatus());
            Order order = orderRepository.findById(changeStatusDTO.getOrderId()).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.ORDER_NOT_FOUND));
            if(order.getOrderStatus() != OrderStatus.NEW && order.getAccount() != null) {
                if(!username.equals(order.getAccount().getUsername())) throw new CustomResponseException(StatusResponseDTO.ORDER_PROCESS_HANDLED_ERROR);
            }
            if(order.getMethodPayment().equals(PaymentMethod.CASH) && OrderStatus.DELIVERED.equals(OrderStatus.valueOf(changeStatusDTO.getStatus()))) order.setPayment_status(PaymentStatus.PAID);
            if(order.getOrderStatus() == OrderStatus.NEW) order.setOrderStatus(status);
            order.setAccount(account);

            return orderMapper.orderToDTO(orderRepository.save(order));

        } catch(Exception ex) {
            throw ex;
        }
    }


    @Override
    public List<OrderResponseDTO> getUserOrders() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND));
        List<Order> orders = user.getOrders();
        List<OrderResponseDTO> userOrders = new ArrayList<>();

        orders.forEach(order -> {
            userOrders.add(orderMapper.orderToDTO(order));
        });
        return userOrders;
    }

    @Override
    public Order findById(String orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.ORDER_NOT_FOUND));
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
    public Order saveOrder(Long addressId, PaymentMethod paymentMethod) {

        /*
        *  save new order with COD payment method
        * */

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(()-> new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND));
        UserCart userCart = user.getCart();
        if(userCart == null) throw new CustomResponseException(StatusResponseDTO.CART_NOT_EXIST);
        List<CartDetails> cartDetailList = userCart.getCartDetails();

        Address userAddress = addressRepository.findById(addressId).orElseThrow(()-> new CustomResponseException(StatusResponseDTO.ADDRESS_NOT_FOUND));;

        Order order = new Order();
        order.setAddress(userAddress);
        order.setOrderDate(new Date());
        order.setCustomer(user);
        order.setOrderStatus(OrderStatus.NEW);
        order.setMethodPayment(paymentMethod);
        order.setPayment_status(paymentMethod.getMethodName().equals("PAY_PAL") ? PaymentStatus.PAID : PaymentStatus.UNPAID);
        order.setOrderDate(new Date());

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

    @Override
    public Object checkoutPaypal(PaypalCheckoutDTO paypalCheckout) throws IOException, InterruptedException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        var appContext = new PayPalAppContextDTO();
        appContext.setReturnUrl("http://localhost:8080/api/v1/orders/checkout/success");
        appContext.setBrandName("My brand");
        paypalCheckout.getOrderDTO().setApplicationContext(appContext);
        // request to get access token
        var token = paypalUtility.getAccessToken();

        // request to checkout url -> get order information
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(createUrl(paypalConfig.getBaseUrl(), ORDER_CHECKOUT)))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken() )
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(paypalCheckout.getOrderDTO())))
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String content = response.body();
        return objectMapper.readValue(content, OrderedResponseDTO.class);
    }


    @Override
    public Object checkoutCOD(OrderRequestDTO orderRequest) throws IOException, InterruptedException {

        // pay with COD method
        return saveOrder(orderRequest.getAddressId(), orderRequest.getMethodPayment());
    }
}
