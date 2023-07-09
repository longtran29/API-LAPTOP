package com.springboot.laptop.mapper;


import com.springboot.laptop.model.Address;
import com.springboot.laptop.model.dto.AddressDTO;
import org.mapstruct.Mapper;

@Mapper
public interface AddressMapper {

    Address dtoToAddress(AddressDTO address);

    AddressDTO addressToDTO(Address address);
}
