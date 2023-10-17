package com.springboot.laptop.service;

import com.springboot.laptop.model.dto.UserCartDTO;
import com.springboot.laptop.model.dto.response.CartResponseDTO;

import java.util.List;

public interface CartService {

    public UserCartDTO addToCart(Long productId, Long quantity);

    public List<CartResponseDTO> getAllCartDetails();

    public UserCartDTO findCartById(Long cartId);

    public UserCartDTO updateQuantityItem(Long productId, String type);

    public Object removeCartItem(Long productId);

}
