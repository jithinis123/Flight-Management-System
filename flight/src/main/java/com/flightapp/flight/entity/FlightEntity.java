package com.flightapp.flight.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "flight")
public class FlightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int flightId;

    int airlineId;

    String name;

    String source;

    String destination;

    LocalDateTime departure;

    LocalDateTime arrival;

    int discount;

    String seats; //A-I

    int seatLimit; //1-42

}
