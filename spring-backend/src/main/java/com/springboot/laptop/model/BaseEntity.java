package com.springboot.laptop.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
//@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
public class BaseEntity {

    public BaseEntity() {

    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTimestamp;

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedTimestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
