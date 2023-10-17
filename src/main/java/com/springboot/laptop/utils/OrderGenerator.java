package com.springboot.laptop.utils;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class OrderGenerator implements IdentifierGenerator {

    private static final String PREFIX ="O_";

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        LocalDateTime dateNow = LocalDateTime.now();
        return PREFIX + dateNow.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }
}
