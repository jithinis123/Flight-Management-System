package com.flightapp.flight.controller;


import com.flightapp.flight.entity.AirlineEntity;
import com.flightapp.flight.entity.FlightEntity;
import com.flightapp.flight.model.AirlineRequest;
import com.flightapp.flight.model.Flight;
import com.flightapp.flight.model.FlightUpdateRequest;
import com.flightapp.flight.model.SearchFlightResponse;
import com.flightapp.flight.service.FlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1.0/flight")
@Slf4j
public class FlightController {

    @Autowired
    FlightService service;

    @GetMapping("/status")
    public ResponseEntity<String> getStatus(){
        return new ResponseEntity<>("Flight application is running", HttpStatus.OK);
    }

    @GetMapping("/flights")
    public ResponseEntity<List<Flight>> getFlights(){
        List<FlightEntity> flights = service.getAllFlights();
        return new ResponseEntity(flights, HttpStatus.OK);
    }

    @PostMapping("/airline/register")
    public ResponseEntity<AirlineEntity> registerAirline(@RequestBody AirlineRequest airline) {
        return new ResponseEntity<>(service.registerAirline(airline), HttpStatus.OK);
    }

//    @PostMapping("/airline/inventory/add")
//    public ResponseEntity<String> addAirlineInventory(@RequestBody List<Flight> flightList) {
//        List<String> status = service.addAirlineSchedule(flightList);
//        if(status.size()==0){
//            return new ResponseEntity<>("Flights saved successfully", HttpStatus.OK);
//        }
//        return new ResponseEntity<>(status.toString(), HttpStatus.OK);
//    }

    @PostMapping("/updateFlight")
    public ResponseEntity<Optional<FlightEntity>> updateFlight(@RequestBody FlightUpdateRequest flight) {
        return new ResponseEntity<>(service.updateFlight(flight), HttpStatus.OK);
    }

    @DeleteMapping("/deleteFlight/{id}")
    public ResponseEntity<String> deleteFlightById(@PathVariable int id) {
        service.deleteFlight(id);
        return new ResponseEntity<>("Flight with Id: "+id+" deleted", HttpStatus.OK);
    }

    @GetMapping("/searchFlight")
    public ResponseEntity<List<SearchFlightResponse>> searchFlight(
            @RequestParam String source, String destination,
            @RequestParam(required = false) String departureAfter,
            @RequestParam(required = false) String arrivalBefore) {
        return new ResponseEntity<>(service.searchFlight(source, destination, departureAfter, arrivalBefore), HttpStatus.OK);
    }

    @GetMapping("/searchFlight/{id}")
    public ResponseEntity<FlightEntity> searchFlightById(@PathVariable int id) {
        return new ResponseEntity<>(service.searchByFlightId(id), HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleControllerExceptions(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
    }
}
