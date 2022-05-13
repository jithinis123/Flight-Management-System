package com.flightapp.flight.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightUpdateRequest {

    int flightId;

    String name;

    String source;

    String destination;

    LocalDateTime departure;

    LocalDateTime arrival;

    int discount;

    String seats; //A-I

    int seatLimit; //1-42
}
