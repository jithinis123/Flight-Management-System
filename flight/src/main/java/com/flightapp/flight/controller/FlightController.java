package com.flightapp.flight.controller;


import com.flightapp.flight.entity.AirlineEntity;
import com.flightapp.flight.util.JwtTokenUtil;
import com.flightapp.flight.entity.FlightEntity;
import com.flightapp.flight.model.Flight;
import com.flightapp.flight.model.JwtRequest;
import com.flightapp.flight.model.JwtResponse;
import com.flightapp.flight.service.FlightService;
import com.flightapp.flight.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1.0/flight")
public class FlightController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    FlightService service;

    @GetMapping("/status")
    public ResponseEntity<String> getStatus(){
        return new ResponseEntity<>("Flight application is running", HttpStatus.OK);
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @GetMapping("/flights")
    public ResponseEntity<List<Flight>> getFlights(){
        List<FlightEntity> flights = service.getAllFlights();
        return new ResponseEntity(flights, HttpStatus.OK);
    }

    @PostMapping("/airline/register")
    public ResponseEntity<AirlineEntity> registerAirline(@RequestBody AirlineEntity airline) {
        return new ResponseEntity<>(service.registerAirline(airline), HttpStatus.OK);
    }

    @PostMapping("/airline/inventory/add")
    public ResponseEntity<String> addAirlineInventory(@RequestBody List<FlightEntity> flightEntityList) {
        service.addAirlineSchedule(flightEntityList);
        return new ResponseEntity<>("Flight schedule(s) saved successfully", HttpStatus.OK);
    }

    @PostMapping("/addFlight")
    public ResponseEntity<FlightEntity> addNewFlight(@RequestBody FlightEntity flight){
        return new ResponseEntity<>(service.addFlight(flight), HttpStatus.CREATED);
    }

    @PostMapping("/updateFlight")
    public ResponseEntity<Optional<FlightEntity>> updateFlight(@RequestBody FlightEntity flight) {
        return new ResponseEntity<>(service.updateFlight(flight), HttpStatus.OK);
    }

    @DeleteMapping("/deleteFlight/{id}")
    public ResponseEntity<String> deleteFlightById(@PathVariable int id) {
        service.deleteFlight(id);
        return new ResponseEntity<>("Flight with Id: "+id+" deleted", HttpStatus.OK);
    }

    @PostMapping("/searchFlight")
    public ResponseEntity<List<FlightEntity>> searchFlight(@RequestParam String source, String destination) {
        return new ResponseEntity<>(service.searchFlight(source, destination), HttpStatus.OK);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @ExceptionHandler
    public ResponseEntity<String> handleControllerExceptions(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
    }
}
