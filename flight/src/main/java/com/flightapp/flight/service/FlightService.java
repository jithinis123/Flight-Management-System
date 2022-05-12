package com.flightapp.flight.service;


import com.flightapp.flight.entity.AirlineEntity;
import com.flightapp.flight.entity.FlightEntity;
import com.flightapp.flight.exception.FlightException;
import com.flightapp.flight.repository.AirlineRepo;
import com.flightapp.flight.repository.FlightRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    @Autowired
    FlightRepo flightRepo;

    @Autowired
    AirlineRepo airlineRepo;

    public List<FlightEntity> getAllFlights(){
        return flightRepo.findAll();
    }

    public FlightEntity addFlight(FlightEntity flight){
        return flightRepo.save(flight);
    }

    public void deleteFlight(int flightId){
        if(flightRepo.findById(flightId).isPresent()) {
            flightRepo.deleteById(flightId);
        } else {
            throw new FlightException("No flight was found for the given Id : "+ flightId);
        }
    }

    @Transactional
    public Optional<FlightEntity> updateFlight(FlightEntity f) {
        Optional<FlightEntity> flight=flightRepo.findById(f.getId()).map(x-> {
            x.setDestination(f.getDestination());
            x.setSource(f.getSource());
            x.setName(f.getName());
            x.setDiscount(f.getDiscount());
            x.setSeatLimit(f.getSeatLimit());
            x.setSeats(f.getSeats());
            x.setDeparture(f.getDeparture());
            x.setArrival(f.getArrival());
            return x;
        });

        if(!flight.isPresent()){
            throw new FlightException("No flight was found for the given Id : "+ f.getId());
        }
        return flight;
    }

    public List<FlightEntity> searchFlight(String source, String destination) {
        List<FlightEntity> flights = flightRepo.searchFlight(source, destination);
        return flights;
    }

    public AirlineEntity registerAirline(AirlineEntity airline) {
        return airlineRepo.save(airline);
    }

    public void addAirlineSchedule(List<FlightEntity> flights) {
        flights.stream().forEach(x->flightRepo.save(x));
    }
}
