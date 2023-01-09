package com.abdullah.coding.challenge.services;


import com.abdullah.coding.challenge.commons.Message;
import com.abdullah.coding.challenge.commons.Util;
import com.abdullah.coding.challenge.dtos.NetworkResponseDto;
import com.abdullah.coding.challenge.dtos.NewBookingDto;
import com.abdullah.coding.challenge.dtos.RatingDto;
import com.abdullah.coding.challenge.dtos.UpdateBookingDto;
import com.abdullah.coding.challenge.entities.Booking;
import com.abdullah.coding.challenge.entities.Cab;
import com.abdullah.coding.challenge.entities.Customer;
import com.abdullah.coding.challenge.enums.BookingStatus;
import com.abdullah.coding.challenge.enums.CabStatus;
import com.abdullah.coding.challenge.repositories.BookingRepository;
import com.abdullah.coding.challenge.validations.BookingValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CabService cabService;

    @Autowired
    private BookingValidation bookingValidation;

    @Autowired
    private CustomerService customerService;

    public Booking findByBookingCode(String bookingCode){
        return bookingRepository.findByBookingCode(bookingCode);
    }
    public Booking save(Booking booking){
        return bookingRepository.save(booking);
    }

    /**
     * Creating new booking and validating prior to it.
     * @param bookingDto
     * @return NetworkResponseDto model
     */
    public NetworkResponseDto createNewBooking(NewBookingDto bookingDto){
        NetworkResponseDto response = new NetworkResponseDto();
        // find if customer exist
        Optional<Customer> customer = customerService.findCustomerById(bookingDto.getCustomerId());
        String validationResponse = bookingValidation.validateNewBooking(bookingDto,  customer.isPresent());
//        when validations are passed, save record and return response
        if(validationResponse.equals(Message.success.BOOKING_DTO_VALIDATED)){
            Booking booking = this.convertNewBookingDtoToModel(bookingDto,customer.get());
            this.save(booking);
            response.setData(booking);
            response.setMsg(Message.success.BOOKING_CREATED);
            response.setCode(HttpStatus.CREATED.value());
            return response;
        }
        response.setMsg(validationResponse);
        response.setCode(HttpStatus.BAD_REQUEST.value());
        return response;

    }

    /**
     * Map NewBookingDto to Booking entity model for record operation in db.
     * @param bookingDto
     * @param customer
     * @return Booking model
     */
    public Booking convertNewBookingDtoToModel(NewBookingDto bookingDto, Customer customer){

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Booking booking = new Booking();

//        New booking by default is marked as REQUESTED status
        booking.setStatus(BookingStatus.REQUESTED.toString());
        booking.setCustomer(customer);
        booking.setVehicleType(bookingDto.getVehicleType());
        booking.setCreationTime(currentTime);
        booking.setModifiedTime(currentTime);
        booking.setRequestTime(bookingDto.getRequestTime());

        String bookingCode = Util.generateBookingCode(booking);
        booking.setBookingCode(bookingCode);

        return booking;
    }

    /**
     * It will change given status of given booking according to different situation.
     * Rather having different method for cancelBooking and completeBooking, we utilize it
     * with same method.
     *
     * @param bookingCode The booking of which status to be changed
     * @param bookingStatus The new status of given booking
     * @return
     */
    public NetworkResponseDto changeBookingStatus(String bookingCode, BookingStatus bookingStatus){
        NetworkResponseDto response = new NetworkResponseDto();
//        setting default code as BAD_REQUEST because of validation
//        but marks success only when rating is applied.
        Booking booking = bookingRepository.findByBookingCode(bookingCode);
        bookingValidation.changeBookingStatusValidation(booking, response, bookingStatus);
//        when validations are passed
        if(response.getCode().equals(HttpStatus.OK.value())){
            if(booking.getStatus().equals(BookingStatus.ASSIGNED.toString())){
                Cab cab = booking.getCab();
                cabService.updateCabStatus(cab,CabStatus.AVAILABLE);
            }

            //      when cab is not assigned it can be canceled but not completed.
//        incomplete ride cannot be marked as completed.
            if(booking.getStatus().equals(BookingStatus.REQUESTED.toString())){
                booking.setStatus(BookingStatus.CANCELLED.toString());

            }
            else{
                booking.setStatus(bookingStatus.toString());

            }
            this.save(booking);
            response.setMsg(bookingStatus.toString());
            response.setData(booking);
            return response;

        }
        return response;

    }

    /**
     * To update booking with new provided details.y
     *
     * @param updateBookingDto
     * @return
     */
    public NetworkResponseDto updateBooking(UpdateBookingDto updateBookingDto){
        NetworkResponseDto response = new NetworkResponseDto();
//        setting default code as BAD_REQUEST because of validation
//        but marks success only when rating is applied.
        response.setCode(HttpStatus.BAD_REQUEST.value());
        Booking booking = bookingRepository.findByBookingCode(updateBookingDto.getBookingCode());
        String validationResponse = bookingValidation.validateUpdateBooking(updateBookingDto, booking);
//        if not validated then return the response of validation error
        if(!validationResponse.equals(Message.success.BOOKING_DTO_VALIDATED)){
            response.setMsg( validationResponse);
            return response;
        }

        booking.setRequestTime(updateBookingDto.getRequestTime());
        this.save(booking);
        response.setData(booking);
        response.setCode(HttpStatus.OK.value());
        response.setMsg(Message.success.BOOKING_UPDATED);
        return response;

    }

    /**
     * Apply rating to booking after validations
     * @param ratingDto
     * @return
     */
    public NetworkResponseDto rateBooking(RatingDto ratingDto){
        NetworkResponseDto response = new NetworkResponseDto();
//        setting default code as BAD_REQUEST because of validation
//        but marks success only when rating is applied.
        response.setCode(HttpStatus.BAD_REQUEST.value());

        Booking booking = bookingRepository.findByBookingCode(ratingDto.getBookingCode());
        String validationResponse = bookingValidation.validateRateBooking(ratingDto, booking);
//        If any error occur during validation then return with error msg
        if(!validationResponse.equals(Message.success.BOOKING_DTO_VALIDATED)){
            response.setMsg( validationResponse);
            return response;
        }
//        Find the cab related to booking to update it
        Optional<Cab> cab = cabService.findById(booking.getCab().getId());
//        Checking to make sure cab is not accidentaly removed from db
        if(cab.isPresent()){
//            we can set average rating if we have all rating history
            cab.get().setRating(ratingDto.getRating());
            cabService.save(cab.get());
//            add values and return response
            response.setData(booking);
            response.setMsg(Message.success.BOOKING_UPDATED);
            response.setCode(HttpStatus.OK.value());
        }
        return response;

    }

}
