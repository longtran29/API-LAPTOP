package com.springboot.laptop.service.impl;

import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.model.Account;
import com.springboot.laptop.model.Customer;
import com.springboot.laptop.model.dto.response.StatusResponseDTO;
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

    @Override
    public Object updateStatus(Long customerId, String status) {
        boolean accountStatus = status.equalsIgnoreCase("enabled");

        Customer existingCustomer = customerRepository.findById(customerId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND));
        existingCustomer.setEnabled(accountStatus);

        return customerRepository.save(existingCustomer);
    }
}
