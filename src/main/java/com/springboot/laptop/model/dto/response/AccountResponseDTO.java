package com.springboot.laptop.model.dto.response;


import com.springboot.laptop.model.Import;
import com.springboot.laptop.model.Order;
import com.springboot.laptop.model.UserRoleEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class AccountResponseDTO {

    private Long id;

    private String username;



    private String email;

    
    private String name;

    
    private String phoneNumber;

    
    private String imgURL;

    private Boolean enabled;



    private List<UserRoleEntity> roles;


}
