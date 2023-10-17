package com.springboot.laptop.service.impl;

import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.mapper.ReviewMapper;
import com.springboot.laptop.model.*;
import com.springboot.laptop.model.dto.request.ReviewRequestDTO;
import com.springboot.laptop.model.dto.response.StatusResponseDTO;
import com.springboot.laptop.model.enums.OrderStatus;
import com.springboot.laptop.repository.OrderRepository;
import com.springboot.laptop.repository.ProductRepository;
import com.springboot.laptop.repository.ReviewRepository;
import com.springboot.laptop.repository.CustomerRepository;
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

    private final CustomerRepository userRepository;

    private final OrderRepository orderRepository;

    private final ReviewMapper reviewMapper;

//    public boolean checkHasComment(ProductEntity product, Customer user) {
//        return reviewRepository.findByProductAndCustomer(product, user) != null;
//    }


    public boolean checkHasComment(String orderCode, Long productId) {
        Order existingOrder = orderRepository.findById(orderCode).orElseThrow(() -> new RuntimeException("The order does not exist"));
        ProductEntity existingProduct = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product does not exist"));
        return reviewRepository.findByProductAndOrder(existingProduct, existingOrder) != null;
    }


    @Override
    public Object postReview(ReviewRequestDTO reviewRequest) {
        ProductEntity existingProduct =  productRepository.findById(reviewRequest.getProductId()).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.PRODUCT_NOT_FOUND));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Order currentOrder =orderRepository.findById(reviewRequest.getOrderCode()).orElseThrow(() -> new RuntimeException("The order not exist"));
        if(currentOrder.getOrderStatus().toString().equals(OrderStatus.DELIVERED.toString())) {
            throw new RuntimeException("This order has not been delivered yet !");
        }
        if(checkHasComment(reviewRequest.getOrderCode(), reviewRequest.getProductId())) throw  new RuntimeException("You have already reviewed this product !");
        ReviewEntity newReview = new ReviewEntity();
        newReview.setComment(reviewRequest.getComment());
        newReview.setRating(reviewRequest.getRating());
        newReview.setReviewTime(new Date());
        newReview.setProduct(existingProduct);
        newReview.setOrder(currentOrder);
        return reviewMapper.convertToDTO(reviewRepository.save(newReview));

    }

    @Override
    public Object getAllReviewBasedProduct(Long productId) {
        return reviewRepository.findByProduct(productRepository.findById(productId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.PRODUCT_NOT_FOUND))).stream().map(reviewMapper::convertToDTO);
    }
}
