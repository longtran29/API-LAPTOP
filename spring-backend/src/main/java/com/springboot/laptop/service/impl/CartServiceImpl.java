package com.springboot.laptop.service.impl;

import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.mapper.CartMapper;
import com.springboot.laptop.mapper.ProductMapper;
import com.springboot.laptop.model.CartDetails;
import com.springboot.laptop.model.ProductEntity;
import com.springboot.laptop.model.UserCart;
import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.dto.UserCartDTO;
import com.springboot.laptop.model.dto.response.CartResponseDTO;
import com.springboot.laptop.model.dto.response.StatusResponseDTO;
import com.springboot.laptop.repository.CartDetailRepository;
import com.springboot.laptop.repository.CartRepository;
import com.springboot.laptop.repository.ProductRepository;
import com.springboot.laptop.repository.UserRepository;
import com.springboot.laptop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartDetailRepository cartDetailRepository;
    private final CartMapper cartMapper;
    private final ProductMapper productMapper;


    @Override
    public UserCartDTO findCartById(Long cartId) {
        productRepository.findById(cartId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.CART_NOT_FOUND));
        return cartMapper.cartToDTO(cartRepository.findById(cartId).get());
    }

    @Override
    public UserCartDTO addToCart(Long productId, Long quantity) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.CUSTOMER_NOT_FOUND));
        ProductEntity existingProduct = productRepository.findById(productId).orElseThrow(()-> new CustomResponseException(StatusResponseDTO.PRODUCT_NOT_FOUND));

        if(quantity > existingProduct.getProductQuantity()) throw new CustomResponseException(StatusResponseDTO.PRODUCT_OUT_STOCK);

        UserCart userCart;
        CartDetails cartDetail = null;
        if(user.getCart() != null) {
            userCart = user.getCart();
            cartDetail = userCart.getCartDetails()
                    .stream()
                    .filter(cd -> cd.getProduct().getId().equals(productId))
                    .findFirst()
                    .orElse(null);
        }
        else {
            userCart = new UserCart();
            userCart.setCreatedTimestamp(new Date());
            userCart.setUser(user);
        }
        if (cartDetail != null) {
            cartDetail.setQuantity(cartDetail.getQuantity() + quantity);
            cartDetail.setModifiedTimestamp(new Date());
        } else {
            cartDetail = new CartDetails();
            cartDetail.setProduct(existingProduct);
            cartDetail.setQuantity(quantity);
            cartDetail.setCreatedTimestamp(new Date());
            cartDetail.setUserCart(userCart);
            userCart.getCartDetails().add(cartDetail);
            userCart.setModifiedTimestamp(new Date());
        }


        long remainingStock = existingProduct.getProductQuantity() - quantity;
        existingProduct.setProductQuantity(remainingStock);
        if(remainingStock == 0) existingProduct.setInStock(false);
        productRepository.save(existingProduct);
        return cartMapper.cartToDTO(cartRepository.save(userCart));
}


    @Override
    public List<CartResponseDTO> getAllCartDetails() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND));
        UserCart userCart = user.getCart();

        List<CartResponseDTO> listCart = new ArrayList<>();
        try {
            for(CartDetails cartDetail : userCart.getCartDetails()) {
                CartResponseDTO cart = new CartResponseDTO();
                cart.setQuantity(cartDetail.getQuantity());
                cart.setProduct(productMapper.productToProductDTO(productRepository.findById(cartDetail.getProduct().getId()).orElse(null)));
                listCart.add(cart);
            }
            return listCart;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    @Override
    public UserCartDTO removeCartItem(Long productId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND));
        productRepository.findById(productId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.PRODUCT_NOT_FOUND));
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
        return cartMapper.cartToDTO(cartRepository.save(userCart));
    }

    @Override
    public UserCartDTO updateQuantityItem(Long productId, String type) {
        UserEntity user = userRepository.findByUsernameIgnoreCase(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND) );
        ProductEntity product = productRepository.findById(productId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.PRODUCT_NOT_FOUND));
        try {
            UserCart userCart = user.getCart();
            if(userCart == null) throw new CustomResponseException(StatusResponseDTO.CART_NOT_FOUND);
            List<CartDetails> listCart = userCart.getCartDetails();
            for (CartDetails cartDetail: listCart
            ) {

                if(Objects.equals(cartDetail.getProduct().getId(), productId)) {
                    if(type.equals("increase")) {
                        if(product.getProductQuantity() ==  0) throw new CustomResponseException(StatusResponseDTO.PRODUCT_OUT_STOCK);
                        cartDetail.setQuantity(cartDetail.getQuantity() +1);
                        product.setProductQuantity(product.getProductQuantity() -1);
                        if(product.getProductQuantity() -1 == 0 ) product.setInStock(false);
                    } else {
                        cartDetail.setQuantity(cartDetail.getQuantity()-1);
                        product.setProductQuantity(product.getProductQuantity() +1);
                    }
                    cartDetail.setModifiedTimestamp(new Date());
                }
            }
            productRepository.save(product);
            return cartMapper.cartToDTO(cartRepository.save(userCart));
        } catch(NotFoundException ex) {
            throw ex;
        }
    }

}
