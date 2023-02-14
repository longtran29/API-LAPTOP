package com.springboot.laptop.model;

import lombok.*;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@Builder
public class RefreshToken {

    @Id
    @Column(nullable = false)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @Column(nullable = false)
    private String value;

}
