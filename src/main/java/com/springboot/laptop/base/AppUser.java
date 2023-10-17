package com.springboot.laptop.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboot.laptop.model.UserRoleEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@MappedSuperclass
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotBlank(message = "Username must be not empty")
    protected String username;

    @NotEmpty(message = "Password must be not empty")
    @Size(min = 6, message = "Password must be at least 5 characters")
    protected String password;

    @NotBlank(message = "Email must be not empty")
    @Email(message = "Vui lòng cung cấp mail đúng định dạng ")
    protected String email;

//    @NotBlank(message = "Name must be not empty")
    protected String name;

    @Column(name="phone_number")
    protected String phoneNumber;

    @Column(name="imgurl")
    protected String imgURL;

    protected Boolean enabled;

    @Column(name ="created_timestamp",nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdTimestamp;

    @Column(name ="modified_timestamp",nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date modifiedTimestamp;

}
