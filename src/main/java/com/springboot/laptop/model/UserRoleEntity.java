package com.springboot.laptop.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboot.laptop.model.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name="role")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private UserRoleEnum name; // ROLE_USER, ROLE_ADMIN

    private String description;

    @OneToMany(mappedBy = "roles")
    private List<Account> employees = new ArrayList<>();
}
