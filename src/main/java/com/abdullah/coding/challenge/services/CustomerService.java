package com.abdullah.coding.challenge.services;


import com.abdullah.coding.challenge.entities.Customer;
import com.abdullah.coding.challenge.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Optional<Customer> findCustomerById(Integer customerId){
        return customerRepository.findById(customerId);
    }

}
