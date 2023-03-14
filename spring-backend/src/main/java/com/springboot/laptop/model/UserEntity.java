package com.springboot.laptop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity {

    private String username;
    private String password;
    private String email;
    private String name;
    private String address;

    private String mobileNumber;


    @JsonBackReference
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

}
