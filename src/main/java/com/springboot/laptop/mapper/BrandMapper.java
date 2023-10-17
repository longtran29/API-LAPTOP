package com.springboot.laptop.mapper;


import com.springboot.laptop.model.BrandEntity;
import com.springboot.laptop.model.dto.BrandDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BrandMapper {

    BrandEntity dtoToEntity(BrandDTO brand);

    BrandDTO entityToDTO(BrandEntity brand);
}
