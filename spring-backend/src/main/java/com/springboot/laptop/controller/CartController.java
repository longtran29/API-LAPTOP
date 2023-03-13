package com.springboot.laptop.controller;

import com.springboot.laptop.model.UserCart;
import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.dto.CartRequestDTO;
import com.springboot.laptop.repository.UserRepository;
import com.springboot.laptop.service.CartServiceImpl;
import com.springboot.laptop.service.UserServiceImpl;
import com.springboot.laptop.service.impl.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
    public ResponseEntity<?> getAllCartItem() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userService.findUserByUserName(username);
        UserCart userCart = user.getCart();
        return ResponseEntity.ok().body(cartService.getAllCartDetails(userCart));
    }
}
