package com.flightapp.admin.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "admin_details")
@Data
public class Admin {

    String name;

    @Id
    String username;

    String password;

}
