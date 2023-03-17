package com.springboot.laptop.controller;

import com.springboot.laptop.model.Order;
import com.springboot.laptop.model.ProductEntity;
import com.springboot.laptop.model.dto.request.OrderRequestDTO;
import com.springboot.laptop.model.dto.response.OrderCompleted;
import com.springboot.laptop.model.dto.response.OrderedProduct;
import com.springboot.laptop.service.OrderServiceImpl;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private JavaMailSender mailSender;

    private final OrderServiceImpl orderService;

    @Autowired
    public OrderController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> checkout(@RequestBody OrderRequestDTO orderRequest) {
        return ResponseEntity.ok().body(orderService.checkout(orderRequest));
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<?> updateStatusOrder() {
//
//        return ResponseEntity.ok().body();
//    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/get_all_orders")
    public ResponseEntity<?> getOrders() {
        List<Order> orders =  orderService.getOrders();
        return ResponseEntity.ok().body(orders);
    }

    @PostMapping("/sendOrderNotification")
    public ResponseEntity<String> sendOrderNotification(@RequestBody OrderCompleted order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(order.getEmail());
        message.setSubject("Đơn hàng");
        // Create a StringBuilder to build the email message text
        StringBuilder messageTextBuilder = new StringBuilder();
        messageTextBuilder.append("Cảm ơn bạn đã sử dụng dịch vụ chúng tôi. Chi tiết đơn hàng như sau:\n\n");

        // Append the ordered products information to the email message text
        messageTextBuilder.append("Các sản phẩm đã đặt:\n");
        for (OrderedProduct product : order.getOrderedProducts()) {
            messageTextBuilder.append("- ").append(product.getProduct().getName()).append(" (").append(product.getQuantity()).append(")\n");
        }

        // Append the delivery address information to the email message text
        messageTextBuilder.append("\n Địa chỉ vận chuyển:\n");
        messageTextBuilder.append(order.getDeliveryAddress().getAddress()).append("\n");
        messageTextBuilder.append(order.getDeliveryAddress().getCity()).append("\n");
        messageTextBuilder.append(order.getDeliveryAddress().getCountry()).append("\n");
        messageTextBuilder.append(order.getDeliveryAddress().getZipcode()).append("\n\n");

        // Append the rest of the message text
        messageTextBuilder.append("Số lượng: ").append(order.getTotalAmt()).append("\n");
        messageTextBuilder.append("Tổng giá tiền : ").append(order.getCartTotal()).append("\n\n");


        message.setText(messageTextBuilder.toString());
        mailSender.send(message);
        return ResponseEntity.ok("Kiểm tra mail về đơn hàng nhé");
    }



}
