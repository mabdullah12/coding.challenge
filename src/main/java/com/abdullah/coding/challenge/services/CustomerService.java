package com.abdullah.coding.challenge.services;

import com.abdullah.coding.challenge.Response;
import com.abdullah.coding.challenge.entities.Customer;
import com.abdullah.coding.challenge.enums.CustomerStatus;
import com.abdullah.coding.challenge.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository repository;


    public boolean isCustomerActive(int customerId) {
        Optional<Customer> customer = repository.findById(customerId);
        if (customer.isEmpty()) {
            return false;
        }

        return customer.get().getStatus().equals(CustomerStatus.ACTIVE.toString());
    }

    public Customer getCustomer(int customerId) {
        Optional<Customer> customer = repository.findById(customerId);
        return customer.orElse(null);
    }

    public Response<?> setBlockStatus(int customerId, boolean blockStatus) {
        try {

            Optional<Customer> customer = repository.findById(customerId);
            if (customer.isEmpty()) {
                return new Response<>(HttpStatus.NOT_FOUND, "No such customer found!", null);
            }

            Customer customerUpdate = customer.get();

            String responseMsg;
            if (blockStatus) {
                customerUpdate.setStatus(CustomerStatus.INACTIVE.toString());
                responseMsg = "Customer Blocked!";

            } else {
                customerUpdate.setStatus(CustomerStatus.ACTIVE.toString());
                responseMsg = "Customer Unblocked!";
            }

            repository.save(customerUpdate);

            return new Response<>(HttpStatus.OK, responseMsg, null);

        } catch (Exception ex) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred", null);
        }
    }
}
