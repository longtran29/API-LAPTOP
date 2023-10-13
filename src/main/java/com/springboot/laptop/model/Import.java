package com.springboot.laptop.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="imports")
@Getter
@Setter
public class Import {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="import_date",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date import_Date;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name="account_id", referencedColumnName = "id")
    private Account account;


    @JsonManagedReference
    @OneToMany(mappedBy = "import_id", cascade = CascadeType.ALL)
    private List<ImportDetail> importDetails = new ArrayList<>();


    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    private Invoice invoice;

    private String note;
}
