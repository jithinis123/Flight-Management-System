package com.flightapp.admin.service;

import com.flightapp.admin.entity.Customer;
import com.flightapp.admin.exception.CustomException;
import com.flightapp.admin.model.BookingRequest;
import com.flightapp.admin.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    CustomerRepo repo;

    public Customer addNewCustomer(Customer customer) {
        Optional<Customer> cust = repo.findByMailId(customer.getMailId());
        if(!cust.isPresent()) {
            customer = repo.save(customer);
        } else {
            throw new CustomException("Mail Id already registered");
        }
        return customer;
    }

    public Customer fetchCustomerDetails(String mailId) {
        Optional<Customer> customer = repo.findByMailId(mailId);
        if(!customer.isPresent()) {
           throw new CustomException("User not found, please register");
        }
        return customer.get();
    }

}
