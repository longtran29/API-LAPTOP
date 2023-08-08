package com.springboot.laptop.controller;

import com.springboot.laptop.model.dto.OrderDTO;
import com.springboot.laptop.model.dto.request.ChangeStatusDTO;
import com.springboot.laptop.model.dto.request.OrderRequestDTO;
import com.springboot.laptop.model.dto.request.PayPalCaptureDTO;
import com.springboot.laptop.model.dto.request.PaymentRequestDTo;
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

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;
    private final PayPalService payPalService;

    @PostMapping("/create_order")
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO) throws IOException, InterruptedException {
        return ResponseEntity.ok().body(payPalService.createOrder(orderDTO));
    }

    @PostMapping(value = "/success")
    public ResponseEntity<?> success(@RequestBody PayPalCaptureDTO successDTO) throws IOException, InterruptedException {
        return ResponseEntity.ok().body(payPalService.confirmOrder(successDTO.getOrderID(), successDTO.getAddressID(), successDTO.getPaymentMethod()));
    }

    @Operation(summary = "API thanh toán")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody OrderRequestDTO orderRequest) throws IOException, InterruptedException {
        return ResponseEntity.ok().body(orderService.checkout(orderRequest));
    }

    @Operation(summary = "API hủy đơn hàng")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
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

    // REMOVED - PLACED BY PAYPAL
//    @PostMapping(value = "/create-payment-intent", consumes={"application/json"})
//    public CreatePaymentResponse  StripPayment(@RequestBody PaymentRequestDTo paymentRequest) throws StripeException {
//
//        String public_key = "pk_test_51NEH4zD418vZNZvg2Si23JwSsO7EdrNjIh9JkIfOimNQ4hwn4nhbXuKFLaq8nOjmBns1eWGy7QW3Nnu8iATt7WET000nvU6aAz";
//        String private_key = "sk_test_51NEH4zD418vZNZvgwa8aLlDUWPGw4Uvfpu9WMeDlFCGGxxwKepdDUzwtB3oQA6J6bJwpRr5C5PFdGaBsg8Ky7XWF00h8byWwVM";
//        Stripe.apiKey = private_key ;
//
//        PaymentIntentCreateParams createParams = new
//                PaymentIntentCreateParams.Builder()
//                .setCurrency("VND")
//                .setAmount((long) (paymentRequest.getTotalPrice() * 1000))
//                .build();
//
//        PaymentIntent intent = PaymentIntent.create(createParams);			// cần có Stripe.apiKey đ tạo ra PaymentIntent, Stripe sẽ tự sử dụng apiKey trong process này
//
//        // Send publishable key and PaymentIntent details to client
//        CreatePaymentResponse paymentResponse = new CreatePaymentResponse(public_key, intent.getClientSecret());
//        System.out.println("Client key " + intent.getClientSecret());
//		return paymentResponse;
//    }
}
