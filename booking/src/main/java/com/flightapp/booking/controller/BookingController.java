package com.flightapp.booking.controller;

import com.flightapp.booking.model.BookFlight;
import com.flightapp.booking.model.BookingDetails;
import com.flightapp.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1.0/booking")
public class BookingController {

    @Autowired
    BookingService service;

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return new ResponseEntity<>("Booking application is running", HttpStatus.OK);
    }

    @PostMapping("/bookFlight/{flightId}")
    public ResponseEntity<String> bookFlight(@PathVariable int flightId , @RequestBody BookFlight book) {
        return new ResponseEntity<>(service.bookFlight(book, flightId).toString(), HttpStatus.OK);
    }

    @GetMapping("/getTicketHistory/{mailid}")
    public ResponseEntity<List<BookingDetails>> getBookingDetails(@PathVariable String mailid) {
        return new ResponseEntity<>(service.seeHistory(mailid), HttpStatus.OK);
    }

    @GetMapping("/getDetails/{pnr}")
    public ResponseEntity<BookingDetails> getBookingDetails(@PathVariable int pnr) {
        return new ResponseEntity<>(service.getBookingDetails(pnr), HttpStatus.OK);
    }

    @DeleteMapping("/cancelBooking/{pnr}")
    public ResponseEntity<String> cancelBooking(@PathVariable int pnr) {
        return new ResponseEntity<>(service.cancelBooking(pnr), HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleControllerExceptions(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
    }

}
