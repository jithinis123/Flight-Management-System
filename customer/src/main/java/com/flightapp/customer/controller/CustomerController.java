package com.flightapp.customer.controller;

import com.flightapp.customer.entity.Customer;
import com.flightapp.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cust")
public class CustomerController {

    @Autowired
    CustomerService service;

    @GetMapping("/status")
    public ResponseEntity<String> getStatus(){
        return new ResponseEntity<>("Customer application is running", HttpStatus.OK);
    }

    @GetMapping("/fetchCustomer/{username}")
    public ResponseEntity<Customer> fetchCustomerDetails(@PathVariable String username) {
        return new ResponseEntity<>(service.fetchCustomerDetails(username), HttpStatus.OK);
    }

    @PostMapping("/addNewCustomer")
    public ResponseEntity<Customer> addNewCustomer(@RequestBody Customer customer) {
        return new ResponseEntity<>(service.addNewCustomer(customer), HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleControllerExceptions(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
    }
}
