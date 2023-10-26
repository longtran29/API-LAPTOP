package com.springboot.laptop.mapper;


import com.springboot.laptop.model.Account;
import com.springboot.laptop.model.dto.response.AccountResponseDTO;
import org.mapstruct.Mapper;

@Mapper
public interface AccountMapper {

   AccountResponseDTO convertEntityToDTO(Account account);


//   convertDTOToEntity();


}
