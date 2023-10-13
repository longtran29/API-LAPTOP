package com.springboot.laptop.mapper;

import com.springboot.laptop.model.CartDetails;
import com.springboot.laptop.model.UserCart;
import com.springboot.laptop.model.dto.CartDetailDTO;
import com.springboot.laptop.model.dto.UserCartDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CartMapper {

    UserCartDTO cartToDTO(UserCart userCart);

    CartDetails cartDTOToCart(UserCartDTO userCartDTO);
}
