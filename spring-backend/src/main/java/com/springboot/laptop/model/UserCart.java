package com.springboot.laptop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCart extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @JsonIgnore
//    option if need bidirection, the JPA use annotation to get list cartDetails associated with userCar
    @OneToMany(mappedBy = "userCart", cascade = CascadeType.ALL)
    private List<CartDetails> cartDetails = new ArrayList<>();
}
