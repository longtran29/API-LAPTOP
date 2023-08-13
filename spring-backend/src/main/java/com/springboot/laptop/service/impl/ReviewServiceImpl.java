package com.springboot.laptop.service.impl;

import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.mapper.ReviewMapper;
import com.springboot.laptop.model.*;
import com.springboot.laptop.model.dto.request.ReviewRequestDTO;
import com.springboot.laptop.model.dto.response.StatusResponseDTO;
import com.springboot.laptop.repository.OrderRepository;
import com.springboot.laptop.repository.ProductRepository;
import com.springboot.laptop.repository.ReviewRepository;
import com.springboot.laptop.repository.UserRepository;
import com.springboot.laptop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final OrderRepository orderRepository;

    private final ReviewMapper reviewMapper;

    public boolean checkHasComment(ProductEntity product, UserEntity user) {
        return reviewRepository.findByProductAndCustomer(product, user) != null;
    }

    @Override
    public Object postReview(ReviewRequestDTO reviewRequest) {
        ProductEntity existingProduct =  productRepository.findById(reviewRequest.getProductId()).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.PRODUCT_NOT_FOUND));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity existingUser = userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.CUSTOMER_NOT_FOUND));
        List<Order> userOrders = orderRepository.findByUser(existingUser);

        if(checkHasComment(existingProduct, existingUser)) throw  new RuntimeException("You have already reviewed this product !");

        boolean checkOrder = false;
        for (Order order: userOrders
             ) {

            for (OrderDetails detail: order.getOrderDetails()
                 ) {
                if(detail.getProduct().getId().equals(reviewRequest.getProductId())) {
                    checkOrder = true;
                    break;
                }
            }
        }

        if(!checkOrder) throw new RuntimeException("You have not purchase for this product");
        ReviewEntity createdReview = new ReviewEntity();
        createdReview.setComment(reviewRequest.getComment());
        createdReview.setRating(reviewRequest.getRating());
        createdReview.setReviewTime(new Date());
        createdReview.setProduct(existingProduct);
        createdReview.setCustomer(existingUser);
        return reviewMapper.convertToDTO(reviewRepository.save(createdReview));
    }

    @Override
    public Object getAllReviewBasedProduct(Long productId) {
        return reviewRepository.findByProduct(productRepository.findById(productId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.PRODUCT_NOT_FOUND))).stream().map(reviewMapper::convertToDTO);
    }
}
