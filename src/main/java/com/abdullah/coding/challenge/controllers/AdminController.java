package com.abdullah.coding.challenge.controllers;

import com.abdullah.coding.challenge.commons.Message;
import com.abdullah.coding.challenge.dtos.NetworkResponseDto;
import com.abdullah.coding.challenge.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    /*
    Assuming admin controller is secured with auth
     */

    @Autowired
    private AdminService adminService;

    @PostMapping("/cancel/{bookingCode}")
    ResponseEntity<NetworkResponseDto> cancelBooking(@PathVariable String bookingCode) {
        NetworkResponseDto response = adminService.cancelBooking(bookingCode);
        if(response.equals(Message.success.BOOKING_CANCELED)){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/confirm/{bookingCode}")
    ResponseEntity<NetworkResponseDto> confirmBooking(@PathVariable String bookingCode) {
        NetworkResponseDto response = adminService.confirmBooking(bookingCode);
        if (response.equals(Message.success.BOOKING_CONFIRMED)) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}