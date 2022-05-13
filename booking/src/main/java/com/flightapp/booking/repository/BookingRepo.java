package com.flightapp.booking.repository;

import com.flightapp.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Integer> {
    List<Booking> findByMailId(String mailId);
}
