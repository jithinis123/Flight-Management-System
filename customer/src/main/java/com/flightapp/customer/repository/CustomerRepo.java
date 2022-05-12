package com.flightapp.customer.repository;

import com.flightapp.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByUsername(String name);
}
