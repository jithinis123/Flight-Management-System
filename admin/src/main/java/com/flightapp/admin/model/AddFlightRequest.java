package com.flightapp.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddFlightRequest {

    int airlineId;

    String name;

    String source;

    String destination;

    String departure;

    String arrival;

    int discount;

    String seats; //A-I

    int seatLimit; //1-42
}
