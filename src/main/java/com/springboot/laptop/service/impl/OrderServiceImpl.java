package com.springboot.laptop.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.laptop.config.PaypalConfig;
import com.springboot.laptop.config.VnPayConfig;
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
import com.springboot.laptop.repository.AddressRepository;
import com.springboot.laptop.repository.CartRepository;
import com.springboot.laptop.repository.OrderRepository;
import com.springboot.laptop.repository.CustomerRepository;
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

    private final VnPayConfig Config;


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
        try {
        OrderStatus status = OrderStatus.valueOfCode(changeStatusDTO.getStatus());
            Order order = orderRepository.findById(changeStatusDTO.getOrderId()).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.ORDER_NOT_FOUND));
            order.setOrderStatus(status);

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
    public Object checkoutVnpay(HttpServletRequest req) throws UnsupportedEncodingException {

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amount = Integer.parseInt(req.getParameter("amount"))*100;


        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_IpAddr = Config.getIpAddress(req);

        String vnp_TmnCode = Config.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

//        if (bankCode != null && !bankCode.isEmpty()) {
//            vnp_Params.put("vnp_BankCode", bankCode);
//        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);


        String locate = req.getParameter("language");
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_Returnurl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
        return paymentUrl;
    }

    @Override
    public Object checkoutCOD(OrderRequestDTO orderRequest) throws IOException, InterruptedException {

        // pay with COD method
        return saveOrder(orderRequest.getAddressId(), orderRequest.getMethodPayment());
    }
}
