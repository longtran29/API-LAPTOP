package com.springboot.laptop.service;

import com.springboot.laptop.model.CartDetails;
import com.springboot.laptop.model.ProductEntity;
import com.springboot.laptop.model.UserCart;
import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.dto.CartResponseDTO;
import com.springboot.laptop.repository.CartRepository;
import com.springboot.laptop.repository.ProductRepository;
import com.springboot.laptop.repository.UserRepository;
import com.springboot.laptop.service.impl.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartServiceImpl(UserRepository userRepository, CartRepository cartRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Override
    public UserCart addToCart(Long productId, Long quantity) {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username).get();

        UserCart userCart = user.getCart();

        if (userCart != null) {

            List<CartDetails> cartDetails = userCart.getCartDetails();
            for (CartDetails cartDetail : cartDetails
            ) {
                if (cartDetail.getProduct().getId() == productId) {
                    cartDetail.setQuantity(cartDetail.getQuantity() + quantity);
                    cartDetail.setModifyDate(LocalDateTime.now());
                }
            }

            return cartRepository.save(userCart);

        } else {
            userCart = new UserCart();
            userCart.setUser(user);
            ProductEntity product = productRepository.findById(productId).orElseThrow(() -> new NoSuchElementException("Product not found"));
            CartDetails cartDetails = new CartDetails();
            cartDetails.setProduct(product);
            cartDetails.setQuantity(quantity);
            cartDetails.setAddDate(LocalDateTime.now());
            cartDetails.setUserCart(userCart);

            userCart.getCartDetails().add(cartDetails);

            return cartRepository.save(userCart);
        }

    }



    public List<CartResponseDTO> getAllCartDetails(UserCart userCart) {
        List<CartResponseDTO> listCart = new ArrayList<>();
        for(CartDetails cartDetail : userCart.getCartDetails()) {
            CartResponseDTO cart = new CartResponseDTO();
            cart.setQuantity(cartDetail.getQuantity());
            cart.setProduct(productRepository.findById(cartDetail.getProduct().getId()).orElse(null));
            listCart.add(cart);
        }
        return listCart;
    }

    public UserCart updateQuantityItem(UserEntity user, Long productId, String type) {
        try {
            UserCart userCart = user.getCart();
            List<CartDetails> listCart = userCart.getCartDetails();
            for (CartDetails cartDetail: listCart
            ) {

                if(cartDetail.getProduct().getId() == productId) {
                    cartDetail.setQuantity(type.equals("increase") ? cartDetail.getQuantity() +1 : cartDetail.getQuantity()-1 );
                    cartDetail.setModifyDate(LocalDateTime.now());
                }
            }
            return cartRepository.save(userCart);
        } catch(NotFoundException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }


}
