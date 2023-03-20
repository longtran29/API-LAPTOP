package com.springboot.laptop.model.dto.response;

import com.springboot.laptop.model.Address;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInformationDTO {

    private List<Address> addresses;

    private String email;

    private String username;

    private String name;

    private String phoneNumber;

    private String imgURL;


}
