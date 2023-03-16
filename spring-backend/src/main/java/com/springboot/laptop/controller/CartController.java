package com.springboot.laptop.controller;

import com.springboot.laptop.model.UserCart;
import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.dto.request.CartRequestDTO;
import com.springboot.laptop.service.CartServiceImpl;
import com.springboot.laptop.service.UserServiceImpl;
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


    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/add_to_cart")
    public ResponseEntity<?> addProductToCart(@RequestBody CartRequestDTO cartRequest) {
        UserCart userCart = cartService.addToCart(cartRequest.getProductId(), cartRequest.getQuantity());
        return ResponseEntity.ok().body(cartService.getAllCartDetails(userCart));
    }

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

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeCartItem(@PathVariable Long productId) {
        UserCart userCart = cartService.removeCartItem(productId);
       return ResponseEntity.ok().body(cartService.getAllCartDetails(userCart));
    }
}
