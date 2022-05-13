package com.flightapp.admin.repository;

import com.flightapp.admin.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByUsername(String name);

    Optional<Customer> findByMailId(String mailId);
}
