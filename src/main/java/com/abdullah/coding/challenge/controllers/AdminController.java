package com.abdullah.coding.challenge.controllers;

import com.abdullah.coding.challenge.Response;
import com.abdullah.coding.challenge.dto.CabDTO;
import com.abdullah.coding.challenge.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private AdminService service;

    @PatchMapping("management/cancel-booking")
    public ResponseEntity<?> cancelBooking(@RequestParam("bookCode") String bookingCode) {
        Response<?> response = service.cancelBooking(bookingCode);
        return new ResponseEntity<>(response, response.getStatusCode());
    }

    @PatchMapping("management/assign-cab")
    public ResponseEntity<?> assignCabToBooking(@RequestParam("id") int bookingId) {
        Response<?> response = service.assignCab(bookingId);
        return new ResponseEntity<>(response, response.getStatusCode());
    }

    @PatchMapping("management/cab-status")
    public ResponseEntity<?> blockCab(@RequestParam("cabId") int cabId, @RequestParam("block") boolean blockStatus) {
        CabDTO cabDTO = new CabDTO(cabId, blockStatus);
        Response<?> response = service.setCabBlockStatus(cabDTO);
        return new ResponseEntity<>(response, response.getStatusCode());
    }

    @PatchMapping("management/customer-status")
    public ResponseEntity<?> blockCustomer(@RequestParam("customerId") int customerId, @RequestParam("block") boolean blockStatus) {
        Response<?> response = service.setCustomerBlockStatus(customerId, blockStatus);
        return new ResponseEntity<>(response, response.getStatusCode());
    }
}
