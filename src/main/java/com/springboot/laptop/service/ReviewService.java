package com.springboot.laptop.service;


import com.springboot.laptop.model.dto.request.ReviewRequestDTO;

public interface ReviewService {


    Object postReview(ReviewRequestDTO reviewRequest);

    Object getAllReviewBasedProduct(Long productId);
}
