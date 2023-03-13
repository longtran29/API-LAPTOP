package com.springboot.laptop.service.impl;

import com.springboot.laptop.model.UserCart;

public interface CartService {

    public UserCart addToCart(Long productId, Long quantity);

}
