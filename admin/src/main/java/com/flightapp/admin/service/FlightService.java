package com.flightapp.admin.service;

import com.flightapp.admin.model.AddFlightRequest;
import com.flightapp.admin.model.Flight;
import com.flightapp.admin.model.RegisterAirlineRequest;
import com.flightapp.admin.model.RegisterAirlineResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class FlightService {

    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<RegisterAirlineResponse> registerNewAirline(RegisterAirlineRequest airlineRequest) {
        HttpEntity<?> entity = new HttpEntity(airlineRequest, null);
        return restTemplate.exchange("lb://FLIGHT/api/v1.0/flight/airline/register", HttpMethod.POST, entity, RegisterAirlineResponse.class);
    }

    public String addFlights(List<AddFlightRequest> flights) {
        HttpEntity<?> entity = new HttpEntity<>(flights, null);
        return restTemplate.exchange("lb://FLIGHT/api/v1.0/flight/airline/inventory/add", HttpMethod.POST, entity, String.class).getBody();

    }

    public List<Object> searchFlight(String source, String destination, String departure, String arrival) {
        List<Object> flights = new ArrayList<>();
        HttpEntity<?> httpEntity = new HttpEntity<>(null, null);
        String url = "lb://FLIGHT/api/v1.0/flight/searchFlight?source="+source+"&destination="+destination;
        if(departure!=null){
            url = url+"&departureAfter="+departure;
        }
        if(arrival!=null) {
            url = url+"&arrivalBefore="+arrival;
        }
        Object[] response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Object[].class).getBody();
        for(Object obj: response) {
            flights.add(obj);
        }
        return flights;
    }
}
