package com.abdullah.coding.challenge.repositories;

import com.abdullah.coding.challenge.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Booking findByBookingCode(String bookingCode);
}
