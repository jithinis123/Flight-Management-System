package com.flightapp.admin.model;

import lombok.Data;

@Data
public class RegisterAirlineRequest {
    String name;
    String contactNo;
    String address;
    String description;
}
