package com.flightapp.admin.model;

import lombok.Data;

@Data
public class RegisterAirlineResponse {

    int airlineId;
    String name;
    String contactNo;
    String address;
    String description;
}
