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
    @GeneratedValue
    @Column(name="airlineid")
    int airlineId;
    String name;
    File logo;
    String contactNo;
    String address;
    String description;

    @OneToMany(mappedBy = "airlineId", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    List<FlightEntity> flights;

}
