package com.springboot.laptop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.springboot.laptop.model.dto.CartRequestDTO;
import com.springboot.laptop.model.dto.CartResponseDTO;
import com.springboot.laptop.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCart extends BaseEntity {

    // one user can only have one cart , foreign key on the side holds reference to the other entity
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @JsonManagedReference
//    option if need bidirection, the JPA use annotation to get list cartDetails associated with userCar
    @OneToMany(mappedBy = "userCart", cascade = CascadeType.ALL)
    private List<CartDetails> cartDetails = new ArrayList<>();


}
