package com.flightapp.booking.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightapp.booking.entity.Booking;
import com.flightapp.booking.exception.BookingException;
import com.flightapp.booking.model.BookFlight;
import com.flightapp.booking.model.BookingDetails;
import com.flightapp.booking.model.FlightDetails;
import com.flightapp.booking.model.Passenger;
import com.flightapp.booking.repository.BookingRepo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookingService {

    @Autowired
    BookingRepo repo;

    @Autowired
    RestTemplate restTemplate;

    public Integer bookFlight(BookFlight booking, int flightId) {
        List<Booking> check = repo.findByMailId(booking.getMailId());
        List<Booking> alreadyBooked = check.stream().filter(x->x.getFlightId()==flightId).collect(Collectors.toList());
        if(alreadyBooked.size()>0){
            throw new BookingException("Flight already booked");
        }
        int noOfSeats = booking.getNoOfSeats();
        Booking book = new Booking();
        book.setFlightId(flightId);
        book.setMailId(booking.getMailId());
        book.setName(booking.getName());
        book.setMealPref(booking.isMealPref());
        book.setPassengers(booking.getPassengers());
        book.setNoOfSeats(noOfSeats);
        book.setCost(noOfSeats*2000+(booking.isMealPref()?noOfSeats*100:0));

        return repo.save(book).getPnr();
    }

    public BookingDetails getBookingDetails(int pnr) {

        Optional<Booking> booking = repo.findById(pnr);
        if(!booking.isPresent()) {
            throw new BookingException("Pnr details not found in the db");
        }

        return createBookingDetailsObject(booking.get());
    }

    public BookingDetails createBookingDetailsObject(Booking booking){
        HttpEntity<?> entity = new HttpEntity(null, null);

        BookingDetails bookingDetails = new BookingDetails();

        bookingDetails.setPnr(booking.getPnr());
        bookingDetails.setMailId(booking.getMailId());
        bookingDetails.setName(booking.getName());
        bookingDetails.setNoOfSeats(booking.getNoOfSeats());
        bookingDetails.setMealPref(booking.isMealPref());
        bookingDetails.setFlightId(booking.getFlightId());
        bookingDetails.setPassengers(parsePassenger(booking.getPassengers()));
        bookingDetails.setCost(booking.getCost());

        //Add flight details to booking
        try {
            FlightDetails flightDetails = restTemplate.exchange("lb://FLIGHT/api/v1.0/flight/searchFlight/"
                    +booking.getFlightId(), HttpMethod.GET, entity, FlightDetails.class).getBody();
            bookingDetails.setDeparture(flightDetails.getDeparture());
            bookingDetails.setArrival(flightDetails.getArrival());
            bookingDetails.setSource(flightDetails.getSource());
            bookingDetails.setDestination(flightDetails.getDestination());
        } catch (Exception e) {
            log.info("Failed to get flight details for flight: " + booking.getFlightId());
        }


        return bookingDetails;
    }

    public List<BookingDetails> seeHistory(String mailId) {
        List<BookingDetails> bookingDetailsList = new ArrayList<>();

        Optional<List<Booking>> booking = Optional.ofNullable(repo.findByMailId(mailId));
        if(booking.isPresent()) {
            for(Booking b: booking.get()) {
                bookingDetailsList.add(createBookingDetailsObject(b));
            }
            return bookingDetailsList;
        }
        return null;
    }
    public String cancelBooking(int pnr) {
        try {
            repo.deleteById(pnr);
        }catch (Exception e) {
            throw new BookingException("Pnr: "+pnr+" not found in db");
        }
        return "Booking cancelled successfully";
    }

    public List<Passenger> parsePassenger(String passengerString) {
        List<Passenger> pass = new ArrayList<>();
        String[] passengers = passengerString.split("&");

        try {
            for (String s : passengers) {
                String[] passengerDetails = s.split(",");
                Passenger passenger = new Passenger();
                passenger.setName(passengerDetails[0]);
                passenger.setAge(Integer.valueOf(passengerDetails[1]));
                passenger.setSeatNo(passengerDetails[2]);
                passenger.setGender(passengerDetails[3]);
                pass.add(passenger);
            }
        }catch (Exception e) {
            log.info("Passenger data corrupt in db : "+ e.getMessage());
        }
        return pass;
    }

}
