package com.springboot.laptop.controller;


import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.ProductDTO;
import com.springboot.laptop.model.dto.request.CartRequestDTO;
import com.springboot.laptop.model.dto.request.ReviewRequestDTO;
import com.springboot.laptop.service.ProductService;
import com.springboot.laptop.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
@SecurityRequirement(name = "bearerAuth")
public class ReviewController {

    private final ReviewService reviewService;


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping
    public Object postReview(@RequestBody ReviewRequestDTO reviewRequest) {
        return ResponseEntity.ok().body(reviewService.postReview(reviewRequest));
    }

//    @PreAuthorize("permitAll")
    @GetMapping("/list-all/{productId}")
    public Object getAllReviewBasedProduct(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok().body(reviewService.getAllReviewBasedProduct(productId));
    }
}
