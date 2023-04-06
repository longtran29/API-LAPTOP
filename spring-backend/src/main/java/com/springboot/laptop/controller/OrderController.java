package com.springboot.laptop.controller;

import com.springboot.laptop.model.dto.request.ChangeStatusDTO;
import com.springboot.laptop.model.dto.request.OrderRequestDTO;
import com.springboot.laptop.model.dto.response.*;
import com.springboot.laptop.service.impl.OrderServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
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

}
