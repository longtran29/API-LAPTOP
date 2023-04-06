package com.springboot.laptop.service;

import com.springboot.laptop.model.UserCart;
import com.springboot.laptop.model.dto.response.CartResponseDTO;

import java.util.List;

public interface CartService {

    public UserCart addToCart(Long productId, Long quantity);

    public List<CartResponseDTO> getAllCartDetails(UserCart userCart);

    public UserCart findCartById(Long cartId);

    public UserCart removeCartItem(Long productId);

}
