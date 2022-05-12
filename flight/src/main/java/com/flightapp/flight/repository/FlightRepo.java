package com.flightapp.flight.repository;


import com.flightapp.flight.entity.FlightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepo extends JpaRepository<FlightEntity, Integer> {

    @Modifying
    @Query(value = "update flight f set f.name=?1, f.source=?2, f.destination=?3, f.discount=?4, f.seats=?5, f.seatLimit=?6", nativeQuery = true)
    void updateFlight(String name, String source, String destination, int discount, String seats, int seatLimit);


    @Query(value = "SELECT * FROM flight f where f.source=?1 and f.destination=?2", nativeQuery = true)
    List<FlightEntity> searchFlight(@Param("source") String source, @Param("destination") String destination);
}
