package com.abdullah.coding.challenge.services;

import com.abdullah.coding.challenge.Response;
import com.abdullah.coding.challenge.dto.CabDTO;
import com.abdullah.coding.challenge.entities.Booking;
import com.abdullah.coding.challenge.entities.Cab;
import com.abdullah.coding.challenge.enums.BookingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AdminService {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private CabService cabService;
    @Autowired
    private CustomerService customerService;

    public Response<?> cancelBooking(String bookingCode) {
        return bookingService.cancelBooking(bookingCode);
    }

    @Transactional
    public Response<?> assignCab(int bookingId) {

        Response<Booking> bookingResponse = bookingService.getBooking(bookingId);
        if (bookingResponse.getStatusCode() == HttpStatus.OK) {

            Booking booking = bookingResponse.getData();
            String requestedVehicleType = booking.getVehicalType();

            if (!booking.getStatus().equals(BookingStatus.REQUESTED.toString())) {
                return new Response<>(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid Booking State", null);
            }

            Response<Cab> cabResponse = cabService.getAvailableCab(requestedVehicleType);
            if (cabResponse.getStatusCode() == HttpStatus.OK) {
                Cab availableCab = cabResponse.getData();
                return bookingService.assignCabToBooking(bookingId, availableCab);
            } else {
                return cabResponse;
            }
        }

        return bookingResponse;
    }

    @Transactional
    public Response<?> setCabBlockStatus(CabDTO cabDTO) {
        return cabService.setCabBlockStatus(cabDTO);
    }

    @Transactional
    public Response<?> setCustomerBlockStatus(int customerId, boolean blockStatus) {
        if (customerId == 9) {
            return new Response<>(HttpStatus.FORBIDDEN, "Forbidden action", null);
        }

        Response<?> response = customerService.setBlockStatus(customerId, blockStatus);
        bookingService.deleteBookingsByStatus(customerId, BookingStatus.REQUESTED.toString());
        return response;
    }
}
