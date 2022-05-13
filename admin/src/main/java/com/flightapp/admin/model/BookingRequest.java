package com.flightapp.admin.model;

import lombok.Data;

@Data
public class BookingRequest {
    int flightId;
    String mailId;
    int noOfSeats;
    String passengers;
    boolean mealPref;
}
