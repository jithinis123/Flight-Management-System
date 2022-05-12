package com.flightapp.customer.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customer_details")
@Data
public class Customer {

    @Id
    @GeneratedValue
    int custId;

    String name;

    String contactNo;

    String mailId;

    String username;

    String password;
}
