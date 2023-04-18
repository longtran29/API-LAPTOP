package com.springboot.laptop.service.impl;

import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.model.CartDetails;
import com.springboot.laptop.model.ProductEntity;
import com.springboot.laptop.model.UserCart;
import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.dto.response.CartResponseDTO;
import com.springboot.laptop.model.dto.response.StatusResponseDTO;
import com.springboot.laptop.repository.CartDetailRepository;
import com.springboot.laptop.repository.CartRepository;
import com.springboot.laptop.repository.ProductRepository;
import com.springboot.laptop.repository.UserRepository;
import com.springboot.laptop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartDetailRepository cartDetailRepository;

    @Autowired
    public CartServiceImpl(UserRepository userRepository, CartRepository cartRepository, ProductRepository productRepository, CartDetailRepository cartDetailRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartDetailRepository = cartDetailRepository;
    }


    @Override
    public UserCart findCartById(Long cartId) {
        if(!productRepository.findById(cartId).isPresent()) throw new CustomResponseException(StatusResponseDTO.CART_NOT_FOUND);
        else  {
            return cartRepository.findById(cartId).get();
        }
    }

    @Override
    public UserCart addToCart(Long productId, Long quantity) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username).get();

        UserCart userCart = null;

        if(user.getCart() != null) {
            userCart = user.getCart();
            CartDetails cartDetail = userCart.getCartDetails()
                    .stream()
                    .filter(cd -> cd.getProduct().getId().equals(productId))
                    .findFirst()
                    .orElse(null);

            if (cartDetail != null) {
                cartDetail.setQuantity(cartDetail.getQuantity() + quantity);
                cartDetail.setModifyDate(LocalDateTime.now());
            } else {
                ProductEntity product = productRepository.findById(productId)
                        .orElseThrow(() -> new NoSuchElementException());

                cartDetail = new CartDetails();
                cartDetail.setProduct(product);
                cartDetail.setQuantity(quantity);
                cartDetail.setAddDate(LocalDateTime.now());
                cartDetail.setUserCart(userCart);
                userCart.getCartDetails().add(cartDetail);
            }

        }
        else {

            userCart = new UserCart();
            userCart.setUser(user);
            ProductEntity product = productRepository.findById(productId)
                    .orElseThrow(() -> new NoSuchElementException());

            CartDetails cartDetail = new CartDetails();
            cartDetail.setProduct(product);
            cartDetail.setQuantity(quantity);
            cartDetail.setAddDate(LocalDateTime.now());
            cartDetail.setUserCart(userCart);
            userCart.getCartDetails().add(cartDetail);


        }

        return cartRepository.save(userCart);
}


    @Override
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
        if(!productRepository.findById(productId).isPresent()) throw new CustomResponseException(StatusResponseDTO.PRODUCT_NOT_FOUND);
        try {
            UserCart userCart = user.getCart();
            if(userCart == null) throw new CustomResponseException(StatusResponseDTO.CART_NOT_FOUND);
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

    @Override
    public UserCart removeCartItem(Long productId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username).get();
        if(!productRepository.findById(productId).isPresent()) throw new CustomResponseException(StatusResponseDTO.PRODUCT_NOT_FOUND);
        UserCart userCart = user.getCart();
        if(userCart != null) {
            Optional<CartDetails> removeItem = userCart.getCartDetails().stream().filter(item -> item.getProduct().getId() == productId).findFirst();

            if(removeItem.isPresent()) {
                userCart.getCartDetails().remove(removeItem.get());
                cartDetailRepository.delete(removeItem.get());
            }
        }
        else {
            throw new NoSuchElementException();
        }
        return cartRepository.save(userCart);
    }

}
