package com.flightapp.flight.service;


import com.flightapp.flight.entity.AirlineEntity;
import com.flightapp.flight.entity.FlightEntity;
import com.flightapp.flight.exception.FlightException;
import com.flightapp.flight.model.AirlineRequest;
import com.flightapp.flight.model.Flight;
import com.flightapp.flight.model.FlightUpdateRequest;
import com.flightapp.flight.repository.AirlineRepo;
import com.flightapp.flight.repository.FlightRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlightService {

    @Autowired
    FlightRepo flightRepo;

    @Autowired
    AirlineRepo airlineRepo;

    ModelMapper modelMapper = new ModelMapper();

    public List<FlightEntity> getAllFlights(){
        return flightRepo.findAll();
    }

    public List<String> addAirlineSchedule(List<Flight> flights){
        ArrayList<String> errors = new ArrayList<>();
        for(Flight flight:flights) {
            Optional<AirlineEntity> airline = airlineRepo.findById(flight.getAirlineId());
            if (!airline.isPresent()) {
                errors.add("Airline Id " + flight.getAirlineId() + " not found in database for flight " + flight);
            } else {
                FlightEntity flightEntity = modelMapper.map(flight, FlightEntity.class);
                flightRepo.save(flightEntity);
            }
        }
        return errors;
    }

    public void deleteFlight(int flightId){
        if(flightRepo.findById(flightId).isPresent()) {
            flightRepo.deleteById(flightId);
        } else {
            throw new FlightException("No flight was found for the Id : "+ flightId);
        }
    }

    public FlightEntity searchByFlightId(int id) {
        Optional<FlightEntity> flight = flightRepo.findById(id);
        if(!flight.isPresent()) {
            throw new FlightException("No flight found for the id : " + id);
        }
        return flight.get();
    }

    @Transactional
    public Optional<FlightEntity> updateFlight(FlightUpdateRequest f) {
        Optional<FlightEntity> flight=flightRepo.findById(f.getFlightId()).map(x-> {
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
            throw new FlightException("No flight was found for the given Id : "+ f.getFlightId());
        }
        return flight;
    }

    public List<FlightEntity> searchFlight(String source, String destination, String departure, String arrival) {
        List<FlightEntity> flights = flightRepo.searchFlight(source.trim(), destination.trim());
        if(departure!=null){
            flights = flights.stream().filter(x->x.getDeparture().isAfter(convertStringToLocalDateTime(departure.trim()))).collect(Collectors.toList());
        } if(arrival!=null) {
            flights = flights.stream().filter(x->x.getArrival().isBefore(convertStringToLocalDateTime(arrival.trim()))).collect(Collectors.toList());
        }
        return flights;
    }

    public LocalDateTime convertStringToLocalDateTime(String dateTime) {
        dateTime = dateTime.replace("T", " ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTime, formatter);
    }

    public AirlineEntity registerAirline(AirlineRequest airline) {
        AirlineEntity airlineEntity = modelMapper.map(airline, AirlineEntity.class);
        return airlineRepo.save(airlineEntity);
    }
}
