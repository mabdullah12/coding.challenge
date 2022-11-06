package com.abdullah.coding.challenge.repository;

import com.abdullah.coding.challenge.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,  Integer> {
}
