package com.springboot.laptop.model.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserCartDTO {

    private Long id;

    private UserDTO customer;

    private List<CartDetailDTO> cartDetails;
}
