package com.flightapp.flight.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "flight")
public class FlightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @ManyToOne(targetEntity = AirlineEntity.class)
    @JoinColumn(name="airlineid")
    AirlineEntity airlineId;

    String name;

    String source;

    String destination;

    Date departure;

    Date arrival;

    int discount;

    String seats; //A-I

    int seatLimit; //1-42



}
