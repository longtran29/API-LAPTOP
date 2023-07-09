package com.springboot.laptop.controller;

import com.springboot.laptop.model.dto.request.ChangeStatusDTO;
import com.springboot.laptop.model.dto.request.OrderRequestDTO;
import com.springboot.laptop.model.dto.request.PaymentRequestDTo;
import com.springboot.laptop.model.dto.response.*;
import com.springboot.laptop.service.OrderService;
import com.springboot.laptop.service.impl.OrderServiceImpl;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    @Autowired
    private JavaMailSender mailSender;

    private final OrderService orderService;

    @Operation(summary = "API thanh toán")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> checkout(@RequestBody OrderRequestDTO orderRequest) {
        return ResponseEntity.ok().body(orderService.checkout(orderRequest));
    }

    @Operation(summary = "API lấy tất cả đơn hàng cho quản trị viên")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/get_all_orders")
    public ResponseEntity<?> manageOrders4Admin() {
        List<OrderResponseDTO> orders =  orderService.getOrders();
        return ResponseEntity.ok().body(orders);
    }

    @Operation(summary = "API cập nhật trang thái đơn hàng - chỉ cho quản trị viên")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin/change_status")
    public ResponseEntity<?> changeStatusOrderAdmin(@RequestBody ChangeStatusDTO changeStatusDTO) {
        return ResponseEntity.ok().body(orderService.changeStatus(changeStatusDTO));
    }


    @Operation(summary = "API hủy đơn hàng")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/cancel_order/{orderId}")
    public Object cancelOrders(@PathVariable Long orderId) {
//       return ResponseEntity.ok().body(orderService.cancelOrders(orderId));
        return orderService.cancelOrders(orderId);
    }

    @Operation(summary = "API lấy các đơn hàng")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/users/get_order")
    public ResponseEntity<?> viewOrders() {
        return ResponseEntity.ok().body(orderService.getUserOrders());
    }


    @Operation(summary = "API xem chi tiết đơn hàng")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/{orderId}")
    public ResponseEntity<?> getOrderDetail(@PathVariable Long orderId) {
        return ResponseEntity.ok().body(orderService.getOrderDetails(orderId));
    }

    @Operation(summary = "API gủi mail thông báo đơn hàng")
    @PostMapping("/sendOrderNotification")
    public ResponseEntity<?> sendOrderNotification(@RequestBody OrderCompleted order) {
        try {
            return ResponseEntity.ok().body(orderService.sendMail4Order(order));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


    @PostMapping(value = "/create-payment-intent", consumes={"application/json"})
    public CreatePaymentResponse  StripPayment(@RequestBody PaymentRequestDTo paymentRequest) throws StripeException {

        String public_key = "pk_test_51NEH4zD418vZNZvg2Si23JwSsO7EdrNjIh9JkIfOimNQ4hwn4nhbXuKFLaq8nOjmBns1eWGy7QW3Nnu8iATt7WET000nvU6aAz";
        String private_key = "sk_test_51NEH4zD418vZNZvgwa8aLlDUWPGw4Uvfpu9WMeDlFCGGxxwKepdDUzwtB3oQA6J6bJwpRr5C5PFdGaBsg8Ky7XWF00h8byWwVM";
        Stripe.apiKey = private_key ;

        PaymentIntentCreateParams createParams = new
                PaymentIntentCreateParams.Builder()
                .setCurrency("VND")
                .setAmount((long) (paymentRequest.getTotalPrice() * 1000))
                .build();

        PaymentIntent intent = PaymentIntent.create(createParams);			// cần có Stripe.apiKey đ tạo ra PaymentIntent, Stripe sẽ tự sử dụng apiKey trong process này

        // Send publishable key and PaymentIntent details to client
        CreatePaymentResponse paymentResponse = new CreatePaymentResponse(public_key, intent.getClientSecret());
        System.out.println("Client key " + intent.getClientSecret());
		return paymentResponse;
    }



}
