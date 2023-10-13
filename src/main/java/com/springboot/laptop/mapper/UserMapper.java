package com.springboot.laptop.mapper;

import com.springboot.laptop.model.Customer;
import com.springboot.laptop.model.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    Customer userDTOToUser(UserDTO userDTO);

    UserDTO userToUserDTO(Customer user);
}
