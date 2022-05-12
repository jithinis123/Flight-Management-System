package com.flightapp.customer.service;

import com.flightapp.customer.entity.Customer;
import com.flightapp.customer.exception.CustomerException;
import com.flightapp.customer.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    CustomerRepo repo;

    public Customer addNewCustomer(Customer customer){
        repo.save(customer);
        return fetchCustomerDetails(customer.getUsername());
    }

    public Customer fetchCustomerDetails(String username) {
        Optional<Customer> customer = repo.findByUsername(username);
        if(!customer.isPresent()) {
           throw new CustomerException("Username not found");
        }
        return customer.get();
    }
}
