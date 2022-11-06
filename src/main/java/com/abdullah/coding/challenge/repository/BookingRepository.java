package com.abdullah.coding.challenge.repository;

import com.abdullah.coding.challenge.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query(value = "SELECT * FROM booking WHERE booking_code = :bookingCode", nativeQuery = true)
    Optional<Booking> findBookingByCode(@Param("bookingCode") String bookingCode);

    @Query(value = "SELECT * FROM booking WHERE customer_id = :customerId", nativeQuery = true)
    List<Booking> findBookingsByCustomer(@Param("customerId") int customerId);

    List<Booking> findByCabId(int cabId);

    List<Booking> findByCustomerId(int customerId);

    List<Booking> findBookingByVehicalType(String cabType);

    @Modifying
    @Query(value = "DELETE FROM booking WHERE customer_id = :customerId AND status = :reqStatus", nativeQuery = true)
    int deleteAllBookingsByStatus(@Param("customerId") int customerId, @Param("reqStatus") String reqStatus);

    @Query(value = "SELECT * FROM booking WHERE rating != -1 AND cab_id = :id", nativeQuery = true)
    List<Booking> getAllRatingsByCab(@Param("id") int id);
}
