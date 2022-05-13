package com.flightapp.admin.model;

import lombok.Data;

@Data
public class BookingRequestWithName {
    int flightId;
    String name;
    String mailId;
    int noOfSeats;
    String passengers;
    boolean mealPref;
}
