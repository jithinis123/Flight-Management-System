package com.flightapp.flight.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchFlightResponse {
    int flightId;

    int airlineId;

    String airlineName;

    String name;

    String source;

    String destination;

    LocalDateTime departure;

    LocalDateTime arrival;

    int discount;

    String seats; //A-I

    int seatLimit; //1-42

}
