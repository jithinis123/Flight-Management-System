package com.flightapp.admin.service;

import com.flightapp.admin.exception.CustomException;
import com.flightapp.admin.model.BookingDetails;
import com.flightapp.admin.model.BookingRequest;
import com.flightapp.admin.model.BookingRequestWithName;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CustomerService customerService;

    public Integer bookFlight(BookingRequest bookingRequest) {
        ModelMapper modelMapper = new ModelMapper();
        int pnr=0;
        String name = customerService.fetchCustomerDetails(bookingRequest.getMailId()).getName();
        BookingRequestWithName bookingRequestWithName = modelMapper.map(bookingRequest, BookingRequestWithName.class);
        bookingRequestWithName.setName(name);
        HttpEntity<?> entity = new HttpEntity(bookingRequestWithName, null);
        try {
            pnr = restTemplate.exchange("lb://BOOKING/api/v1.0/booking/bookFlight/" + bookingRequest.getFlightId(),
                    HttpMethod.POST, entity, Integer.class).getBody();
        } catch (Exception e) {
            throw new CustomException("Flight already booked");
        }
        return pnr;
    }

    public BookingDetails getBookingDetailsByPnr(int pnr){
        BookingDetails bookingDetails = new BookingDetails();
        HttpEntity<?> httpEntity = new HttpEntity<>(null, null);
        try {
            bookingDetails = restTemplate.exchange("lb://BOOKING/api/v1.0/booking/getDetails/" + pnr, HttpMethod.GET, httpEntity, BookingDetails.class).getBody();
        } catch (Exception e) {
            if(e.getMessage().contains("Unrecognized token 'Pnr'")) {
                throw new CustomException("Pnr not found in database");
            } else{
                throw new CustomException(e);
            }
        }
        return bookingDetails;
    }

    public List<Object> getBookingHistory(String mailId){
        List<Object> bookingDetails = new ArrayList<>();
        Object[] response = restTemplate.getForObject("lb://BOOKING/api/v1.0/booking/getTicketHistory/"+mailId, Object[].class);
        for(Object obj: response) {
            bookingDetails.add(obj);
        }
        return bookingDetails;
    }

    public String cancelBooking(int pnr) {
        HttpEntity<?> httpEntity = new HttpEntity<>(null, null);
        return restTemplate.exchange("lb://BOOKING/api/v1.0/booking/cancelBooking/"+pnr, HttpMethod.DELETE, httpEntity, String.class).getBody();
    }
}
