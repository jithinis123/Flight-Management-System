package com.flightapp.admin.service;

import com.flightapp.admin.model.RegisterAirlineRequest;
import com.flightapp.admin.model.RegisterAirlineResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FlightService {

    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<RegisterAirlineResponse> registerNewAirline(RegisterAirlineRequest airlineRequest) {
        HttpEntity<?> entity = new HttpEntity(airlineRequest, null);
        return restTemplate.exchange("lb://FLIGHT/api/v1.0/flight/airline/register", HttpMethod.POST, entity, RegisterAirlineResponse.class);
    }

    public List<Object> viewSchedule(){
        List<Object> flights = new ArrayList<>();
        HttpEntity<?> entity = new HttpEntity(null, null);
        Object[] responses = restTemplate.exchange("lb://FLIGHT/api/v1.0/flight/viewSchedules", HttpMethod.GET, entity, Object[].class).getBody();
        for(Object obj:responses) {
            flights.add(obj);
        }
        return flights;
    }

    public List<Object> searchFlight(String source, String destination, String departure, String arrival) {
        List<Object> flights = new ArrayList<>();

        HttpEntity<?> httpEntity = new HttpEntity<>(null, null);
        String url = "lb://FLIGHT/api/v1.0/flight/searchFlight?source="+source+"&destination="+destination;
        if(departure!=null){
            url = url+"&departure="+convertDate(departure);
        }
        if(arrival!=null) {
            url = url+"&arrival="+convertDate(arrival);
        }

        Object[] response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Object[].class).getBody();

        for(Object obj: response) {
            flights.add(obj);
            log.info("Response:"+obj.toString());
        }
        return flights;
    }

    public String convertDate(String rawDate) {

        return rawDate+"T00:00:00";
    }
}
