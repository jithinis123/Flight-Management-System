package com.flightapp.flight.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightapp.flight.model.Flight;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class KafkaConsumerListener {

    @Autowired
    FlightService service;

    private static final String TOPIC = "flight-schedules";

    @KafkaListener(topics = TOPIC, groupId="group_id", containerFactory = "userKafkaListenerFactory")
    public void consumeJson(List<String> flights) {
        log.info("Consumed JSON Message: " + flights);
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        List<Flight> flightList = mapper.convertValue(flights, new TypeReference<>() {});
        service.addAirlineSchedule(flightList);
    }
    
}