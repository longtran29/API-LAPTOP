package com.springboot.laptop.controller;

import com.springboot.laptop.exception.OrderStatusException;
import com.springboot.laptop.model.Order;
import com.springboot.laptop.model.OrderDetails;
import com.springboot.laptop.model.dto.request.ChangeStatusDTO;
import com.springboot.laptop.model.dto.request.OrderRequestDTO;
import com.springboot.laptop.model.dto.response.*;
import com.springboot.laptop.service.OrderServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(orderService.changeStatus(changeStatusDTO));
        return ResponseEntity.ok().body(responseDTO);
    }


    @Operation(summary = "API hủy đơn hàng")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/cancel_order/{orderId}")
    public ResponseEntity<?> cancelOrders(@PathVariable Long orderId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO.setData(orderService.cancelOrders(orderId));

        } catch (OrderStatusException ex) {
            responseDTO.setErrorCode(ErrorCode.ORDER_CANCEL_CONFLICT);
            responseDTO.setData(ex.getMessage());
        }

        return ResponseEntity.ok().body(responseDTO) ;
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
        Order order = orderService.findById(orderId);

        System.out.println("Order id " + orderId);

//        orderResponse.setId(orderId);
//        orderResponse.setUser(UserResponseDTO.builder().username(order.getUser().getUsername()).email(order.getUser().getEmail()).build());
//        orderResponse.setOrderDate(order.getOrderDate());
//        orderResponse.setStatusName(order.getOrderStatus().getName());
//
//        orderResponse.setTotal(order.getTotal());
//        orderResponse.setAddress(AddressResponseDTO.builder().address(order.getAddress().getAddress()).city(order.getAddress().getCity()).phoneNumber(order.getAddress().getPhoneNumber()).build());
        List<OrderDetailResponseDTO> orderDetails = new ArrayList<>();
        for (OrderDetails orderDetail : order.getOrderDetails()) {
            orderDetails.add(OrderDetailResponseDTO.builder().product(ProductResponseDTO.builder().prod_id(orderDetail.getProduct().getId()).primaryImage(orderDetail.getProduct().getPrimaryImage()).name(orderDetail.getProduct().getName()).build()).quantity(orderDetail.getQuantity()).total(orderDetail.getTotal()).build());
        }
//        orderResponse.setOrderDetails(orderDetails);
        OrderResponseDTO orderResponse = OrderResponseDTO.builder().id(orderId).user(UserResponseDTO.builder().username(order.getUser().getUsername()).email(order.getUser().getEmail()).build()).orderDate(order.getOrderDate()).status(order.getOrderStatus().name()).statusName(order.getOrderStatus().getName()).total(order.getTotal()).address(AddressResponseDTO.builder().address(order.getAddress().getAddress()).city(order.getAddress().getCity()).phoneNumber(order.getAddress().getPhoneNumber()).build()).orderDetails(orderDetails).build();
        return ResponseEntity.ok().body(orderResponse);
    }


//    @Operation(summary = "API gủi mail thông báo đơn hàng")
//    @PostMapping("/sendOrderNotification")
//    public ResponseEntity<String> sendOrderNotification(@RequestBody OrderCompleted order) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(order.getEmail());
//        message.setSubject("Đơn hàng");
//        // Create a StringBuilder to build the email message text
//        StringBuilder messageTextBuilder = new StringBuilder();
//        messageTextBuilder.append("Cảm ơn bạn đã sử dụng dịch vụ chúng tôi. Chi tiết đơn hàng như sau:\n\n");
//
//        // Append the ordered products information to the email message text
//        messageTextBuilder.append("Các sản phẩm đã đặt:\n");
//        for (OrderedProduct product : order.getOrderedProducts()) {
//            messageTextBuilder.append("- ").append(product.getProduct().getName()).append(" (").append(product.getQuantity()).append(")\n");
//        }
//
//        // Append the delivery address information to the email message text
//        messageTextBuilder.append("\n Địa chỉ vận chuyển:\n");
//        messageTextBuilder.append(order.getDeliveryAddress().getAddress()).append("\n");
//        messageTextBuilder.append(order.getDeliveryAddress().getCity()).append("\n");
//        messageTextBuilder.append(order.getDeliveryAddress().getCountry()).append("\n");
//        messageTextBuilder.append(order.getDeliveryAddress().getZipcode()).append("\n\n");
//
//        // Append the rest of the message text
//        messageTextBuilder.append("Số lượng: ").append(order.getTotalAmt()).append("\n");
//        messageTextBuilder.append("Tổng giá tiền : ").append(order.getCartTotal()).append("\n\n");
//
//
//        message.setText(messageTextBuilder.toString());
//        mailSender.send(message);
//        return ResponseEntity.ok("Kiểm tra mail về đơn hàng nhé");
//    }

    @Operation(summary = "API gủi mail thông báo đơn hàng")
    @PostMapping("/sendOrderNotification")
    public ResponseEntity<?> sendOrderNotification(@RequestBody OrderCompleted order) {
        try {
            return ResponseEntity.ok().body(orderService.sendMail4Order(order));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}
