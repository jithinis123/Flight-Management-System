package com.flightapp.flight.model;

import java.util.List;


public class Flight {

    int id;

    String name;

    String source;

    String destination;

    int discount;

    List<Seats> seats; //A-I

    int seatLimit; //1-42
}
