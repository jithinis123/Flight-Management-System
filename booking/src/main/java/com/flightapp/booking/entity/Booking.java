package com.flightapp.booking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int pnr;
    int flightId;
    String name;
    String mailId;
    int noOfSeats;
    String passengers;
    boolean mealPref;
    int cost;
}
