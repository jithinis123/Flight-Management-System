package com.flightapp.flight.service;


import com.flightapp.flight.entity.AirlineEntity;
import com.flightapp.flight.entity.FlightEntity;
import com.flightapp.flight.exception.FlightException;
import com.flightapp.flight.model.AirlineRequest;
import com.flightapp.flight.model.Flight;
import com.flightapp.flight.model.FlightUpdateRequest;
import com.flightapp.flight.model.SearchFlightResponse;
import com.flightapp.flight.repository.AirlineRepo;
import com.flightapp.flight.repository.FlightRepo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FlightService {

    @Autowired
    FlightRepo flightRepo;

    @Autowired
    AirlineRepo airlineRepo;

    ModelMapper modelMapper = new ModelMapper();

    public List<FlightEntity> getAllFlights(){
        return flightRepo.findAll();
    }

    @Transactional
    public void addAirlineSchedule(List<Flight> flights){
        for(Flight flight:flights) {
            Optional<AirlineEntity> airline = airlineRepo.findById(flight.getAirlineId());
            if (!airline.isPresent()) {
                log.error("Airline Id " + flight.getAirlineId() + " not found in database for flight " + flight);
            } else {
                FlightEntity flightEntity = modelMapper.map(flight, FlightEntity.class);
                flightRepo.save(flightEntity);
            }
        }
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

    public List<SearchFlightResponse> searchFlight(String source, String destination, String departure, String arrival) {
        List<SearchFlightResponse> responses = new ArrayList<>();
        List<FlightEntity> departureList;
        List<FlightEntity> arrivalList = new ArrayList<>();
        departureList = flightRepo.searchFlight(source.trim(), destination.trim());
        if(departure!=null){
            departureList=(departureList.stream().filter(x->x.getDeparture().toLocalDate().equals(convertStringToLocalDate(departure.trim()))).collect(Collectors.toList()));
        }
        if(arrival!=null) {
            arrivalList = flightRepo.searchFlight(destination.trim(), source.trim());
            arrivalList=(arrivalList.stream().filter(x->x.getDeparture().toLocalDate().equals(convertStringToLocalDate(arrival.trim()))).collect(Collectors.toList()));
        }
        departureList.addAll(arrivalList);
        for(FlightEntity f: departureList) {
            String airLineName = airlineRepo.findById(f.getAirlineId()).get().getName();
            SearchFlightResponse response = modelMapper.map(f, SearchFlightResponse.class);
            response.setAirlineName(airLineName);
            responses.add(response);
        }

        return responses;
    }

    public List<SearchFlightResponse> viewSchedule(){
        List<SearchFlightResponse> responses = new ArrayList<>();
        List<FlightEntity> flights = flightRepo.findAll();
        for(FlightEntity f: flights) {
            String airLineName = airlineRepo.findById(f.getAirlineId()).get().getName();
            SearchFlightResponse response = modelMapper.map(f, SearchFlightResponse.class);
            response.setAirlineName(airLineName);
            responses.add(response);
        }

        return responses;
    }

    public LocalDate convertStringToLocalDate(String dateTime) {
        dateTime = dateTime.split("T")[0];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateTime, formatter);
    }

    public AirlineEntity registerAirline(AirlineRequest airline) {
        AirlineEntity airlineEntity = modelMapper.map(airline, AirlineEntity.class);
        return airlineRepo.save(airlineEntity);
    }
}
