package com.springboot.laptop.model;


import javax.persistence.*;

@MappedSuperclass
public class BaseEntity {

    public BaseEntity() {

    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
