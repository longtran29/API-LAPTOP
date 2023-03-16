package com.springboot.laptop.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.springboot.laptop.model.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name="role")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleEntity extends BaseEntity {
    @Column(length= 40, nullable = false, unique = true)
    private String name; // ROLE_USER, ROLE_ADMIN

    private String description;

    @Override
    public String toString() {
        return this.name;
    }


    @JsonBackReference("user_roles")
    @ManyToMany(mappedBy = "roles")
    private List<UserEntity> users = new ArrayList<>();
}
