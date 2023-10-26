package com.springboot.laptop.model.dto;


import com.springboot.laptop.model.dto.response.OrderResponseDTO;
import com.springboot.laptop.model.dto.response.ProductResponseDTO;
import lombok.Data;

import java.util.Date;

@Data
public class ReviewDTO {

    private Long id;


    private String headline;


    private String comment;

    private int rating;

    private int votes;


    private Date reviewTime;


    private ProductResponseDTO product;


    private OrderResponseDTO order;

}
