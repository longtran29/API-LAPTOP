package com.springboot.laptop.model.dto;

import com.springboot.laptop.model.Address;
import com.springboot.laptop.model.Order;
import com.springboot.laptop.model.UserRoleEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private String name;
    private String phoneNumber;

    @Temporal(TemporalType.DATE)
    private Date createdTimestamp;

    private String imgURL;
    private Boolean enabled;

    private List<AddressDTO> addresses ;

    private List<Order> orders ;
}
