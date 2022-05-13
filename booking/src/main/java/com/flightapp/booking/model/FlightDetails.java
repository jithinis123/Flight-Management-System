package com.flightapp.booking.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlightDetails {
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
