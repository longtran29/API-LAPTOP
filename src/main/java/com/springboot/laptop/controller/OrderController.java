package com.springboot.laptop.controller;

import com.springboot.laptop.model.dto.OrderDTO;
import com.springboot.laptop.model.dto.request.*;
import com.springboot.laptop.model.dto.response.*;
import com.springboot.laptop.service.OrderService;
import com.springboot.laptop.service.PayPalService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;
    private final PayPalService payPalService;


    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping("/create_order")
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO) throws IOException, InterruptedException {
        return ResponseEntity.ok().body(payPalService.createOrder(orderDTO));
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping(value = "/confirm_payment_paypal")
    public ResponseEntity<?> success(@RequestBody PayPalCaptureDTO successDTO) throws IOException, InterruptedException {
        return ResponseEntity.ok().body(payPalService.confirmOrder(successDTO.getOrderID(), successDTO.getAddressID(), successDTO.getPaymentMethod()));
    }

    @Operation(summary = "API thanh toán")
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER')")
    @PostMapping("/checkout/vnPay")
    public ResponseEntity<?> checkoutVnPay(HttpServletRequest req) throws UnsupportedEncodingException {
        return ResponseEntity.ok().body(orderService.checkoutVnpay(req));
    }

    @Operation(summary = "API thanh toán")
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER')")
    @PostMapping("/checkout/paypal")
    public ResponseEntity<?> checkoutPaypal(@RequestBody PaypalCheckoutDTO checkout) throws IOException, InterruptedException {
        return ResponseEntity.ok().body(orderService.checkoutPaypal(checkout));
    }

    @Operation(summary = "API thanh toán")
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER')")
    @PostMapping("/checkout/COD")
    public ResponseEntity<?> checkout(@RequestBody OrderRequestDTO orderRequest) throws IOException, InterruptedException {
        return ResponseEntity.ok().body(orderService.checkoutCOD(orderRequest));
    }

    @Operation(summary = "API hủy đơn hàng")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CUSTOMER')")
    @PutMapping("/cancel_order/{orderId}")
    public Object cancelOrders(@PathVariable String orderId) {
       return ResponseEntity.ok().body(orderService.cancelOrders(orderId));
    }

    @Operation(summary = "API lấy các đơn hàng")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/users/get_order")
    public ResponseEntity<?> viewOrders() {
        return ResponseEntity.ok().body(orderService.getUserOrders());
    }


    @Operation(summary = "API gủi mail thông báo đơn hàng")
    @PostMapping("/send_order_detail")
    public ResponseEntity<?> getOrderDetail(@RequestBody OrderCompleted order) {
        return ResponseEntity.ok().body(orderService.sendMail4Order(order));
    }

    @Operation(summary = "API xem chi tiết đơn hàng")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/detail/{orderId}")
    public ResponseEntity<?> getOrderDetail(@PathVariable String orderId) {
        return ResponseEntity.ok().body(orderService.getOrderDetails(orderId));
    }


}
