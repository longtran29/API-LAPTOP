package com.springboot.laptop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="reset_token")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetTokenEntity extends BaseEntity {

    private String token;

    @ManyToOne
    private UserEntity userId;

    private Boolean active = true;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

}
