package com.springboot.laptop.model.dto;


import com.springboot.laptop.model.ProductEntity;
import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.dto.response.ProductResponseDTO;
import lombok.Data;

import javax.persistence.*;
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


    private UserDTO customer;

}
