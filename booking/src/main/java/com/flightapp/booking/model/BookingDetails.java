package com.flightapp.booking.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingDetails {
    int pnr;
    int flightId;
    String source;
    String destination;
    LocalDateTime departure;
    LocalDateTime arrival;
    String name;
    String mailId;
    int noOfSeats;
    List<Passenger> passengers;
    boolean mealPref;
    int cost;
}
