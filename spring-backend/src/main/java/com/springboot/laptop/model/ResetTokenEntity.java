package com.springboot.laptop.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Table(name="reset_token")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotEmpty(message = "Token must be provided")
    private String token;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity userId;

    private Boolean active = true;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

}
