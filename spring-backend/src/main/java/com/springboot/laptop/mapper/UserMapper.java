package com.springboot.laptop.mapper;


import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    UserEntity userDTOToUser(UserDTO userDTO);

    UserDTO userToUserDTO(UserEntity user);
}
