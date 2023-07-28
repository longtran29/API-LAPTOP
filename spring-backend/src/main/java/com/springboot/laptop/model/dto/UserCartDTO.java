package com.springboot.laptop.model.dto;


import com.springboot.laptop.model.CartDetails;
import com.springboot.laptop.model.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserCartDTO {

    private Long id;

    private UserDTO user;

    private List<CartDetailDTO> cartDetails;
}
