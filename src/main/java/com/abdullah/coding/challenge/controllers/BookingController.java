package com.abdullah.coding.challenge.controllers;

import com.abdullah.coding.challenge.Response;
import com.abdullah.coding.challenge.dto.BookingDTO;
import com.abdullah.coding.challenge.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("booking")
public class BookingController {
    @Autowired
    private BookingService service;

    @GetMapping()
    public ResponseEntity<?> getBooking(@RequestParam("bookingId") int bookingId) {
        Response<?> response = service.getBooking(bookingId);
        return new ResponseEntity<>(response, response.getStatusCode());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@RequestBody BookingDTO bookingDTO) {
        Response<?> response = service.createBooking(bookingDTO);
        return new ResponseEntity<>(response, response.getStatusCode());
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateBooking(@RequestBody BookingDTO bookingDTO, @RequestParam("bookingCode") String bookingCode) {
        Response<?> response = service.updateBooking(bookingDTO, bookingCode);
        return new ResponseEntity<>(response, response.getStatusCode());
    }

    @PatchMapping("/cancel")
    public ResponseEntity<?> cancelBooking(@RequestParam("bookingCode") String bookingCode, @RequestParam("customerId") int customerId) {
        Response<?> response = service.cancelBooking(bookingCode, customerId);
        return new ResponseEntity<>(response, response.getStatusCode());
    }

    @GetMapping("/all")
    public ResponseEntity<?> getBookings() {
        Response<?> response = service.getAllBookings();
        return new ResponseEntity<>(response, response.getStatusCode());
    }

    @GetMapping("/cab-bookings")
    public ResponseEntity<?> getBookingsByCab(@RequestParam("cabId") int cabId) {
        Response<?> response = service.getBookingsByCab(cabId);
        return new ResponseEntity<>(response, response.getStatusCode());
    }

    @GetMapping("/customer-bookings")
    public ResponseEntity<?> getBookingsByCustomer(@RequestParam("customerId") int customerId) {
        Response<?> response = service.getBookingsByCustomer(customerId);
        return new ResponseEntity<>(response, response.getStatusCode());
    }

    @GetMapping("/cab-type")
    public ResponseEntity<?> getBookingsByType(@RequestParam("type") String bookingType) {
        Response<?> response = service.getBookingsByCabType(bookingType);
        return new ResponseEntity<>(response, response.getStatusCode());
    }
}
