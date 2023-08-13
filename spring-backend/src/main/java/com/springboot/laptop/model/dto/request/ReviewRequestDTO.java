package com.springboot.laptop.model.dto.request;


import lombok.Data;

@Data
public class ReviewRequestDTO {

    private String comment;

    private int rating;

    private Long productId;
}
