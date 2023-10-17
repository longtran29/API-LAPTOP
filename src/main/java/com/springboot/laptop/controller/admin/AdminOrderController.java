package com.springboot.laptop.controller.admin;

import com.springboot.laptop.model.dto.request.ChangeStatusDTO;
import com.springboot.laptop.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/order")
@SecurityRequirement(name = "bearerAuth")
public class AdminOrderController {

    private final OrderService orderService;

    @Operation(summary = "API lấy tất cả đơn hàng cho quản trị viên")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get_all_order")
    public ResponseEntity<?> manageOrder() {
        return ResponseEntity.ok().body(orderService.getOrders());
    }

    @Operation(summary = "API cập nhật trang thái đơn hàng - chỉ cho quản trị viên")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update_status")
    public ResponseEntity<?> updateStatusOrder(@RequestBody ChangeStatusDTO changeStatusDTO) {
        return ResponseEntity.ok().body(orderService.changeStatus(changeStatusDTO));
    }

}
