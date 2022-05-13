package com.flightapp.flight.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.File;
import java.util.List;

@Data
@Entity
@Table(name = "airline")
public class AirlineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int airlineId;
    String name;
    String contactNo;
    String address;
    String description;
}
