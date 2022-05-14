package com.flightapp.admin.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "user_details")
public class Customer {
    @Id
    String mailId;
    String name;
    String contactNo;
}
