package com.springboot.laptop.model;


import com.springboot.laptop.model.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name="role")
@Entity
@Data
public class UserRoleEntity extends BaseEntity {
    @Column(length= 40, nullable = false, unique = true)
    private String name; // ROLE_USER, ROLE_ADMIN

    private String description;

    @Override
    public String toString() {
        return this.name;
    }
}
