package com.springboot.laptop.mapper;

import com.springboot.laptop.model.ReviewEntity;
import com.springboot.laptop.model.dto.ReviewDTO;
import org.mapstruct.Mapper;

@Mapper
public interface ReviewMapper {

    ReviewEntity convertToEntity(ReviewDTO review);

    ReviewDTO convertToDTO(ReviewEntity review);
}
