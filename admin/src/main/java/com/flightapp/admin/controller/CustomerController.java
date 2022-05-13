package com.flightapp.admin.controller;

import com.flightapp.admin.entity.Customer;
import com.flightapp.admin.model.BookingDetails;
import com.flightapp.admin.model.BookingRequest;
import com.flightapp.admin.model.Flight;
import com.flightapp.admin.service.BookingService;
import com.flightapp.admin.service.CustomerService;
import com.flightapp.admin.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    BookingService bookingService;

    @Autowired
    FlightService flightService;

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return new ResponseEntity<>("User application is running", HttpStatus.OK);
    }

    @GetMapping("/fetchCustomer/{mailId}")
    public ResponseEntity<Customer> fetchCustomerDetails(@PathVariable String mailId) {
        return new ResponseEntity<>(customerService.fetchCustomerDetails(mailId), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Customer> registerCustomer(@RequestBody Customer customer) {
        return new ResponseEntity<>(customerService.addNewCustomer(customer), HttpStatus.OK);
    }

    @PostMapping("/bookFlight")
    public ResponseEntity<String> bookFlight(@RequestBody BookingRequest bookingRequest) {
        customerService.fetchCustomerDetails(bookingRequest.getMailId());
        return new ResponseEntity<>("PNR NUMBER :" +bookingService.bookFlight(bookingRequest), HttpStatus.OK);
    }

    @GetMapping("/searchFlight")
    public ResponseEntity<Object> searchFlight(
            @RequestParam String source, String destination,
            @RequestParam(required = false) String departureAfter,
            @RequestParam(required = false) String arrivalBefore) {
        return new ResponseEntity<>(flightService.searchFlight(source, destination, departureAfter, arrivalBefore), HttpStatus.OK);
    }

    @GetMapping("/getBookingDetails/{pnr}")
    public ResponseEntity<BookingDetails> getBookingDetailsByPnr(@PathVariable int pnr) {
        return new ResponseEntity(bookingService.getBookingDetailsByPnr(pnr), HttpStatus.OK);
    }

    @GetMapping("/getTicketHistory/{mailid}")
    public ResponseEntity<List<BookingDetails>> getBookingDetailsByPnr(@PathVariable String mailid) {
        return new ResponseEntity(bookingService.getBookingHistory(mailid), HttpStatus.OK);
    }

    @DeleteMapping("/cancelBooking/{pnr}")
    public ResponseEntity<String> cancelBooking(@PathVariable int pnr) {
        return new ResponseEntity(bookingService.cancelBooking(pnr), HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleControllerExceptions(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
    }
}
