package com.springboot.laptop.model.dto.response;

import com.springboot.laptop.model.Import;
import com.springboot.laptop.model.Order;
import com.springboot.laptop.model.UserRoleEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class EmployeeResponseDTO {
        private Long id;

        private String name;

        private String username;

        private String email;

        private String phoneNumber;

        private boolean status_account;

        private String role;

}
