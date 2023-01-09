package com.abdullah.coding.challenge.controllers;

import com.abdullah.coding.challenge.commons.Message;
import com.abdullah.coding.challenge.dtos.NetworkResponseDto;
import com.abdullah.coding.challenge.dtos.NewBookingDto;
import com.abdullah.coding.challenge.dtos.RatingDto;
import com.abdullah.coding.challenge.dtos.UpdateBookingDto;
import com.abdullah.coding.challenge.enums.BookingStatus;
import com.abdullah.coding.challenge.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/booking")
public class BookingController {
     /*
    Assuming booking controller is secured and customer login session is validated prior to request
     */

    @Autowired
    private BookingService bookingService;

    @PostMapping
    ResponseEntity<NetworkResponseDto> newBooking(@RequestBody NewBookingDto newBookingDto){
        NetworkResponseDto response = bookingService.createNewBooking(newBookingDto);
        if(response.getMsg().equals(Message.success.BOOKING_CREATED)){
            return new ResponseEntity<>(response,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/cancel/{bookingCode}")
    ResponseEntity<NetworkResponseDto> cancelBooking(@PathVariable String bookingCode){
        NetworkResponseDto response = bookingService.changeBookingStatus(bookingCode, BookingStatus.CANCELLED);
        if(response.equals(Message.success.BOOKING_CANCELED)){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/complete/{bookingCode}")
    ResponseEntity<NetworkResponseDto> completeBooking(@PathVariable String bookingCode){
        NetworkResponseDto response = bookingService.changeBookingStatus(bookingCode, BookingStatus.COMPLETED);
        if(response.equals(Message.success.BOOKING_COMPLETED)){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PatchMapping
    ResponseEntity<NetworkResponseDto> update(@RequestBody UpdateBookingDto updateBookingDto){
        NetworkResponseDto response = bookingService.updateBooking(updateBookingDto);
        if(response.equals(Message.success.BOOKING_UPDATED)){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/rate")
    ResponseEntity<NetworkResponseDto> rate(@RequestBody RatingDto ratingDto){
        NetworkResponseDto response = bookingService.rateBooking(ratingDto);
        if(response.equals(Message.success.BOOKING_UPDATED)){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
