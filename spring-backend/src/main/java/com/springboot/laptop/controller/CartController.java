package com.springboot.laptop.controller;

import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.UserCart;
import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.dto.request.CartRequestDTO;
import com.springboot.laptop.service.CartServiceImpl;
import com.springboot.laptop.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
    private CartServiceImpl cartService;
    private UserServiceImpl userService;

    @Autowired
    public CartController(CartServiceImpl cartService, UserServiceImpl userService) {
        this.cartService = cartService;
        this.userService = userService;
    }


    @Operation(summary = "Thêm vào giỏ",
            description = "Thên sản phẩm vào giỏ hàng",security = {
            @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(description = "Thêm thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/add_to_cart")
    public ResponseEntity<?> addProductToCart(@RequestBody CartRequestDTO cartRequest) {
        UserCart userCart = cartService.addToCart(cartRequest.getProductId(), cartRequest.getQuantity());
        return ResponseEntity.ok().body(cartService.getAllCartDetails(userCart));
    }


    @Operation(summary = "Giỏ hàng ",
            description = "Xem danh sách sản phẩm trong giỏ hàng",security = {
            @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(description = "Thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/all_cart_items")
    public ResponseEntity<?> getAllCartItem() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Name is " + username);

        UserEntity user = userService.findUserByUserName(username);
        try {
            UserCart userCart = user.getCart();
            return ResponseEntity.ok().body(cartService.getAllCartDetails(userCart));
        }

        catch(NullPointerException ex) {
            return ResponseEntity.badRequest().body("Chưa có giỏ hàng nào");
        }
    }

    @Operation(summary = "Thêm / giảm số lượng ",
            description = "Thêm hoặc giảm số lượng sản phẩm trong giỏ hàng",security = {
            @SecurityRequirement(name = "bearer-key") },
//            tags = {"Category"},
            responses = {
                    @ApiResponse(description = "Thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping("/update/{productId}/{type}")
    public ResponseEntity<?> updateQuantityItem(@PathVariable Long productId, @PathVariable String type) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userService.findUserByUserName(username);

       try {
           UserCart userCart = cartService.updateQuantityItem(user, productId, type);

           return ResponseEntity.ok().body(cartService.getAllCartDetails(userCart));

       } catch (Exception ex) {
           return ResponseEntity.badRequest().body(ex.getMessage());
       }
    }



    @Operation(summary = "Xoá item trong giỏ hàng ",
            description = "Xoá các items trong giỏ hàng",security = {
            @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(description = "Xoá thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeCartItem(@PathVariable Long productId) {
        UserCart userCart = cartService.removeCartItem(productId);
       return ResponseEntity.ok().body(cartService.getAllCartDetails(userCart));
    }
}
