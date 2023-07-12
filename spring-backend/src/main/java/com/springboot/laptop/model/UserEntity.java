package com.springboot.laptop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity extends BaseEntity {
    @NotBlank(message = "Username must be not empty")
    private String username;

    @NotBlank(message = "Product name must be not empty")
    private String password;

    @NotBlank(message = "Email must be not empty")
    @Email
    private String email;

    private String name;
    private String phoneNumber;

    private String imgURL;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<UserRoleEntity> roles = new ArrayList<>();


    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private UserCart cart;


    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address> addresses = new ArrayList<>();

    private Boolean enabled;

    @JsonManagedReference
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

}
