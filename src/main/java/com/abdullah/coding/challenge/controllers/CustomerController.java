package com.abdullah.coding.challenge.controllers;

import com.abdullah.coding.challenge.Response;
import com.abdullah.coding.challenge.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private BookingService bookingService;

    @PostMapping("/booking-comment")
    public ResponseEntity<?> postComment(@RequestParam("bookingId") int bookingId, @RequestParam("comment") String comment) {
        Response<?> response = bookingService.postComment(bookingId, comment);
        return new ResponseEntity<>(response, response.getStatusCode());
    }

    @PostMapping("/booking-rating")
    public ResponseEntity<?> postRating(@RequestParam("bookingId") int bookingId, @RequestParam("rating") int rating) {
        Response<?> response = bookingService.postRating(bookingId, rating);
        return new ResponseEntity<>(response, response.getStatusCode());
    }

}
