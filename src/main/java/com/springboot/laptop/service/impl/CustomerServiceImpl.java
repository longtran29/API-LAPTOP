package com.springboot.laptop.service.impl;

import com.springboot.laptop.repository.CustomerRepository;
import com.springboot.laptop.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;


    @Override
    public Object getAllCustomer() {
        return customerRepository.findAll();
    }
}
