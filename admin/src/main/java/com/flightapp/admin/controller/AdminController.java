package com.flightapp.admin.controller;

import com.flightapp.admin.entity.Admin;
import com.flightapp.admin.model.AddFlightRequest;
import com.flightapp.admin.model.JwtResponse;
import com.flightapp.admin.model.RegisterAirlineRequest;
import com.flightapp.admin.model.RegisterAirlineResponse;
import com.flightapp.admin.service.AdminService;
import com.flightapp.admin.service.FlightService;
import com.flightapp.admin.service.JwtUserDetailsService;
import com.flightapp.admin.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1.0/admin")
@SecurityRequirement(name="admin-service")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    FlightService flightService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private KafkaTemplate<String, List<AddFlightRequest>> kafkaTemplate;

    private static final String TOPIC = "flight-schedules";

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody Admin adminDetails) throws Exception {

        authenticate(adminDetails.getUsername(), adminDetails.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(adminDetails.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @GetMapping(value = "/status", produces = "text/plain")
    public ResponseEntity<String> getStatus(){
        return new ResponseEntity<>("Admin application is running", HttpStatus.OK);
    }

    @PostMapping("/addAdmin")
    public ResponseEntity<String> addAdmin(@RequestBody Admin admin) {
        adminService.addAdmin(admin);
        return new ResponseEntity<>("Admin added successfully", HttpStatus.OK);
    }

    @PostMapping("/registerAirline")
    public ResponseEntity<RegisterAirlineResponse> registerAirline(@RequestBody RegisterAirlineRequest airlineRequest){
        return flightService.registerNewAirline(airlineRequest);
    }

    @PostMapping("/addFlightSchedule")
    public ResponseEntity<String> addFlights(@RequestBody List<AddFlightRequest> addFlightRequest) {
        kafkaTemplate.send(TOPIC, addFlightRequest);
        return new ResponseEntity<>("Flight schedule(s) sent successfully", HttpStatus.OK);
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
    public ResponseEntity<String> handleControllerException(Exception e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
    }
}
