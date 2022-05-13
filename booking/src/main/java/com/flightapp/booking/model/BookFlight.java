package com.flightapp.booking.model;

import lombok.Data;

@Data
public class BookFlight {
    String name;
    String mailId;
    int noOfSeats;
    String passengers;
    boolean mealPref;
}
