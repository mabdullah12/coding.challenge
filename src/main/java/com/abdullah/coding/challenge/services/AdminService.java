package com.abdullah.coding.challenge.services;

import com.abdullah.coding.challenge.commons.Message;
import com.abdullah.coding.challenge.dtos.NetworkResponseDto;
import com.abdullah.coding.challenge.entities.Booking;
import com.abdullah.coding.challenge.entities.Cab;
import com.abdullah.coding.challenge.enums.BookingStatus;
import com.abdullah.coding.challenge.enums.CabStatus;
import com.abdullah.coding.challenge.enums.VehicleType;
import com.abdullah.coding.challenge.validations.BookingValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
public class AdminService {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private CabService cabService;

    @Autowired
    private BookingValidation bookingValidation;

    public NetworkResponseDto cancelBooking(String bookingCode){
        return bookingService.changeBookingStatus(bookingCode, BookingStatus.CANCELLED);
    }

    /**
     * To confirm the given booking and validate it
     * @param bookingCode
     * @return
     */
    public NetworkResponseDto confirmBooking(String bookingCode) {
        NetworkResponseDto response = new NetworkResponseDto();
        Booking booking = bookingService.findByBookingCode(bookingCode);
        bookingValidation.confirmBookingValidation(booking,response);
//        if validation not passed then return validation error
        if(response.getCode().equals(HttpStatus.BAD_REQUEST)){
            return response;
        }
//        get available cabs for assigning i.e. we can check nearest cab to customer location
        Cab cab = cabService.getCab(VehicleType.getVehicleType(booking.getVehicleType()));
        if(cab!=null){
            booking.setStatus(BookingStatus.ASSIGNED.toString());
            booking.setCab(cab);
            cab.setStatus(CabStatus.NOT_AVAILABLE.toString());
            bookingService.save(booking);
            cabService.save(cab);
            response.setData(booking);
            response.setCode(HttpStatus.OK.value());
            response.setMsg(Message.success.BOOKING_CONFIRMED);
        }else{
            response.setMsg(Message.error.CAB_NOT_AVAILABLE);
        }
        return response;
    }

}
