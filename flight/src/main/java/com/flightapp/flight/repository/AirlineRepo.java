package com.flightapp.flight.repository;


import com.flightapp.flight.entity.AirlineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirlineRepo extends JpaRepository<AirlineEntity, Integer> {

}
