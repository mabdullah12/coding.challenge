package com.abdullah.coding.challenge.services;

import com.abdullah.coding.challenge.Response;
import com.abdullah.coding.challenge.dto.BookingDTO;
import com.abdullah.coding.challenge.entities.Booking;
import com.abdullah.coding.challenge.entities.Cab;
import com.abdullah.coding.challenge.entities.Customer;
import com.abdullah.coding.challenge.enums.BookingStatus;
import com.abdullah.coding.challenge.enums.CustomerStatus;
import com.abdullah.coding.challenge.repository.BookingRepository;
import com.abdullah.coding.challenge.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingService {
    @Autowired
    private BookingRepository repository;
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CabService cabService;

    public Response<Booking> createBooking(BookingDTO bookingDTO) {
        try {

            if (bookingDTO.getCustomerId() <= 0) {
                return new Response<>(HttpStatus.BAD_REQUEST, "Invalid Customer ID", null);
            }

            if (!TimeUtil.isValidTimeStamp(bookingDTO.getRequestTime())) {
                return new Response<>(HttpStatus.BAD_REQUEST, "Invalid Date format", null);
            }

            Customer customer = customerService.getCustomer(bookingDTO.getCustomerId());
            if (customer == null) {
                return new Response<>(HttpStatus.NOT_FOUND, "No such Customer exists", null);
            }

            if (customer.getStatus().equals(CustomerStatus.INACTIVE.toString())) {
                return new Response<>(HttpStatus.FORBIDDEN, "User blocked!", null);
            }

            Timestamp requestedTime = TimeUtil.getTimeStamp(bookingDTO.getRequestTime());
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            if (requestedTime == null || requestedTime.before(currentTime)) {
                return new Response<>(HttpStatus.BAD_REQUEST, "Invalid Request Time, expected YYYY/MM/dd HH:mm", null);
            }

            Booking booking = new Booking();
            booking.setStatus(String.valueOf(BookingStatus.REQUESTED));
            booking.setBookingCode(UUID.randomUUID().toString());
            booking.setVehicalType(bookingDTO.getVehicleType());
            booking.setRequestTime(requestedTime);
            booking.setCreationTime(currentTime);
            booking.setCustomer(customer);

            repository.save(booking);

            return new Response<>(HttpStatus.CREATED, "Cab booking request sent!", booking);
        } catch (Exception ex) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing the request!", null);
        }
    }

    public Response<?> updateBooking(BookingDTO bookingDTO, String bookingCode) {
        try {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());

            if (bookingDTO.getCustomerId() <= 0) {
                return new Response<>(HttpStatus.BAD_REQUEST, "Invalid Customer ID", null);
            }

            if (!TimeUtil.isValidTimeStamp(bookingDTO.getRequestTime())) {
                return new Response<>(HttpStatus.BAD_REQUEST, "Invalid Date format", null);
            }

            Customer customer = customerService.getCustomer(bookingDTO.getCustomerId());
            if (customer == null) {
                return new Response<>(HttpStatus.NOT_FOUND, "No such Customer exists", null);
            }

            if (customer.getStatus().equals(CustomerStatus.INACTIVE.toString())) {
                return new Response<>(HttpStatus.FORBIDDEN, "User blocked!", null);
            }

            Optional<Booking> existingBooking = repository.findBookingByCode(bookingCode);
            if (existingBooking.isEmpty()) {
                return new Response<>(HttpStatus.NOT_FOUND, "No booking found", null);
            }

            Booking bookingUpdate = existingBooking.get();
            bookingUpdate.setModifiedTime(currentTime);

            if (bookingUpdate.getCustomer().getId() != bookingDTO.getCustomerId()) {
                return new Response<>(HttpStatus.FORBIDDEN, "Illegal Action not allowed", null);
            }

            if (!bookingUpdate.getStatus().equals(BookingStatus.REQUESTED.toString())) {
                return new Response<>(HttpStatus.UNPROCESSABLE_ENTITY, "Booking can not be updated anymore", null);
            }

            if (bookingDTO.getVehicleType() != null) {
                bookingUpdate.setVehicalType(bookingDTO.getVehicleType());
            }

            if (bookingDTO.getRequestTime() != null) {
                Timestamp requestedTime = TimeUtil.getTimeStamp(bookingDTO.getRequestTime());
                if (requestedTime == null || requestedTime.before(currentTime)) {
                    return new Response<>(HttpStatus.BAD_REQUEST, "Invalid Request Time, expected YYYY/MM/dd HH:mm", null);
                }

                bookingUpdate.setRequestTime(requestedTime);
            }

            repository.save(bookingUpdate);

            return new Response<>(HttpStatus.NO_CONTENT, "Booking updated!", null);
        } catch (Exception ex) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing the request!", null);
        }
    }

    @Transactional
    public Response<?> cancelBooking(String bookingCode, int customerId) {
        try {
            if (customerId <= 0) {
                return new Response<>(HttpStatus.BAD_REQUEST, "Invalid Customer ID", null);
            }

            Customer customer = customerService.getCustomer(customerId);
            if (customer == null) {
                return new Response<>(HttpStatus.NOT_FOUND, "No such Customer exists", null);
            }

            if (customer.getStatus().equals(CustomerStatus.INACTIVE.toString())) {
                return new Response<>(HttpStatus.FORBIDDEN, "User blocked!", null);
            }

            Optional<Booking> existingBooking = repository.findBookingByCode(bookingCode);
            if (existingBooking.isEmpty()) {
                return new Response<>(HttpStatus.NOT_FOUND, "No booking found", null);
            }

            Booking bookingUpdate = existingBooking.get();

            if (bookingUpdate.getCustomer().getId() != customerId) {
                return new Response<>(HttpStatus.FORBIDDEN, "Illegal Action not allowed", null);
            }

            if (bookingUpdate.getStatus().equals(BookingStatus.COMPLETED.toString())) {
                return new Response<>(HttpStatus.UNPROCESSABLE_ENTITY, "Booking is already completed", null);
            }

            if (bookingUpdate.getCab() != null) {
                Cab cab = bookingUpdate.getCab();
                cabService.makeCabAvailable(cab.getId());
            }

            bookingUpdate.setStatus(BookingStatus.CANCELLED.toString());

            repository.save(bookingUpdate);

            return new Response<>(HttpStatus.NO_CONTENT, "Booking cancelled!", null);
        } catch (Exception ex) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing the request!", null);
        }
    }

    @Transactional
    public Response<?> cancelBooking(String bookingCode) {
        try {

            Optional<Booking> existingBooking = repository.findBookingByCode(bookingCode);
            if (existingBooking.isEmpty()) {
                return new Response<>(HttpStatus.NOT_FOUND, "No booking found", null);
            }

            Booking bookingUpdate = existingBooking.get();

            if (bookingUpdate.getStatus().equals(BookingStatus.COMPLETED.toString())) {
                return new Response<>(HttpStatus.UNPROCESSABLE_ENTITY, "Booking is already completed", null);
            }

            Cab cab = bookingUpdate.getCab();
            if (cab != null) {
                cabService.makeCabAvailable(cab.getId());
            }

            bookingUpdate.setStatus(BookingStatus.CANCELLED.toString());

            repository.save(bookingUpdate);

            return new Response<>(HttpStatus.NO_CONTENT, "Booking cancelled!", null);
        } catch (Exception ex) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing the request!", null);
        }
    }

    @Transactional
    public Response<?> assignCabToBooking(int bookingId, Cab cab) {
        try {

            Optional<Booking> existingBooking = repository.findById(bookingId);
            if (existingBooking.isEmpty()) {
                return new Response<>(HttpStatus.NOT_FOUND, "No booking found", null);
            }

            cabService.makeCabUnavailable(cab.getId());
            Booking bookingUpdate = existingBooking.get();
            bookingUpdate.setCab(cab);
            bookingUpdate.setStatus(BookingStatus.ASSIGNED.toString());
            repository.save(bookingUpdate);

            return new Response<>(HttpStatus.OK, "Cab Assigned!", null);
        } catch (Exception ex) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing the request!", null);
        }
    }

    public Response<Booking> getBooking(int bookingId) {
        try {
            Optional<Booking> booking = repository.findById(bookingId);
            if (booking.isEmpty()) {
                return new Response<>(HttpStatus.NOT_FOUND, "No booking found!", null);
            }

            return new Response<>(HttpStatus.OK, "Success", booking.get());

        } catch (Exception ex) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing the request!", null);
        }
    }

    public Response<List<Booking>> getAllBookings() {
        try {

            List<Booking> bookings = repository.findAll();
            return new Response<>(HttpStatus.OK, "Success", bookings);

        } catch (Exception ex) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing the request!", null);
        }
    }

    public Response<List<Booking>> getBookingsByCab(int cabId) {
        try {

            List<Booking> bookings = repository.findByCabId(cabId);
            return new Response<>(HttpStatus.OK, "Success", bookings);

        } catch (Exception ex) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing the request!", null);
        }
    }

    public Response<List<Booking>> getBookingsByCustomer(int customerId) {
        try {

            List<Booking> bookings = repository.findBookingsByCustomer(customerId);
            return new Response<>(HttpStatus.OK, "Success", bookings);

        } catch (Exception ex) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing the request!", null);
        }
    }

    public Response<List<Booking>> getBookingsByCabType(String cabType) {
        try {

            List<Booking> bookings = repository.findBookingByVehicalType(cabType);
            return new Response<>(HttpStatus.OK, "Success", bookings);

        } catch (Exception ex) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing the request!", null);
        }
    }

    public int deleteBookingsByStatus(int customerId, String status) {
        return repository.deleteAllBookingsByStatus(customerId, status);
    }

    public Response<?> postComment(int bookingId, String comment) {
        try {
            Optional<Booking> booking = repository.findById(bookingId);
            if (booking.isEmpty()) {
                return new Response<>(HttpStatus.NOT_FOUND, "No booking found!", null);
            }

            Booking bookingUpdate = booking.get();
            bookingUpdate.setComment(comment);
            repository.save(bookingUpdate);

            return new Response<>(HttpStatus.CREATED, "Comment posted!", null);

        } catch (Exception ex) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing the request!", null);
        }
    }

    @Transactional
    public Response<?> postRating(int bookingId, int rating) {
        try {

            if (rating < 1 || rating > 5) {
                return new Response<>(HttpStatus.BAD_REQUEST, "Invalid Rating", null);
            }

            Optional<Booking> booking = repository.findById(bookingId);
            if (booking.isEmpty()) {
                return new Response<>(HttpStatus.NOT_FOUND, "No booking found!", null);
            }

            Booking bookingUpdate = booking.get();

            if (!bookingUpdate.getStatus().equals(BookingStatus.COMPLETED.toString())) {
                return new Response<>(HttpStatus.UNPROCESSABLE_ENTITY, "Booking must be completed!", null);
            }

            if (bookingUpdate.getRating() != -1) {
                return new Response<>(HttpStatus.UNPROCESSABLE_ENTITY, "Booking already rated!", null);
            }

            bookingUpdate.setRating(rating);
            repository.save(bookingUpdate);

            double actualRating = getCalculatedRating(bookingUpdate.getCab().getId());
            cabService.setCabRating(actualRating, bookingUpdate.getCab().getId());

            return new Response<>(HttpStatus.CREATED, "Rating posted!", null);

        } catch (Exception ex) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing the request!", null);
        }
    }

    private double getCalculatedRating(int cabId) {
        List<Booking> ratedBookings = repository.getAllRatingsByCab(cabId);
        double total = 0;

        if (ratedBookings.isEmpty()) {
            return 0;
        }

        for (Booking num : ratedBookings) {
            total += num.getRating();
        }

        return (total / ratedBookings.size());
    }
}
