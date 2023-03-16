package com.springboot.laptop.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCart extends BaseEntity {

    // one user can only have one cart , foreign key on the side holds reference to the other entity
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @JsonManagedReference("cart_detail_user_cart")
//    option if need bidirection, the JPA use annotation to get list cartDetails associated with userCar
    @OneToMany(mappedBy = "userCart", cascade = CascadeType.ALL)
    private List<CartDetails> cartDetails = new ArrayList<>();


}
