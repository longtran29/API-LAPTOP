package com.springboot.laptop.repository;


import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class GeneratorIdnetifer {


    @Test
    public void genOrderId() {

        LocalDateTime dateNow = LocalDateTime.now();

        String timeStamp = dateNow.format( DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));

        System.out.println("Value is " + timeStamp);

    }


}
