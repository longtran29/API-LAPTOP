package com.springboot.laptop.model.dto.response;

import com.springboot.laptop.model.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInformationDTO {

    private List<Address> addresses;

    private String email;
}
