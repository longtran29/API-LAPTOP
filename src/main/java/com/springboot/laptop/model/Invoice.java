package com.springboot.laptop.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.springboot.laptop.model.enums.PaymentMethod;
import com.springboot.laptop.model.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonManagedReference
    // bi-directional
    @OneToOne(mappedBy = "invoice")
    private Import import_id;

    @Column(name ="invoice_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date invoiceDate;

    private double total_amount;


    @Column(name ="due_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;


    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod methodPayment;

}
