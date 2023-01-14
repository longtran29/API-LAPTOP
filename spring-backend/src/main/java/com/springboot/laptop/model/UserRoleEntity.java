package com.springboot.laptop.model;


import com.springboot.laptop.model.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Table(name="role")
@Entity
@Data
public class UserRoleEntity extends BaseEntity {

    private UserRoleEnum role;

    @Enumerated(EnumType.STRING)
    public UserRoleEnum getName() {
        return role;
    }

    public void setName(UserRoleEnum name) {
        this.role = role;
    }
}
