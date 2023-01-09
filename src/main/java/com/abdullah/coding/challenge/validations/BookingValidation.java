package com.abdullah.coding.challenge.validations;

import com.abdullah.coding.challenge.commons.Constant;
import com.abdullah.coding.challenge.commons.Message;
import com.abdullah.coding.challenge.commons.Util;
import com.abdullah.coding.challenge.dtos.NetworkResponseDto;
import com.abdullah.coding.challenge.dtos.NewBookingDto;
import com.abdullah.coding.challenge.dtos.RatingDto;
import com.abdullah.coding.challenge.dtos.UpdateBookingDto;
import com.abdullah.coding.challenge.entities.Booking;
import com.abdullah.coding.challenge.enums.BookingStatus;
import com.abdullah.coding.challenge.enums.VehicleType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class BookingValidation {


    /**
     * To validate new booking params
     * @param newBookingDto
     * @param customerExist
     * @return
     */
    public String validateNewBooking(NewBookingDto newBookingDto, Boolean customerExist){

        //        check if requested time is in future comparing in same timezone
        boolean timeInFuture =  newBookingDto.getRequestTime().toLocalDateTime().isAfter(new Timestamp(System.currentTimeMillis()).toLocalDateTime());

//        check if vehicle type is valid and cab available of that type
        if(VehicleType.getVehicleType(newBookingDto.getVehicleType())==null){
            return Message.error.VEHICLE_TYPE_INVALID;
        }
//        check if customer exist
        else if (!customerExist) {
            return Message.error.CUSTOMER_NOT_FOUND;
        }
//        request time should be in future
        else if (!timeInFuture) {
            return Message.error.REQUEST_TIME_IN_PAST;

        }
        return Message.success.BOOKING_DTO_VALIDATED;


    }

    /**
     * Validate the update booking request and its params
     * @param updateBookingDto
     * @param booking
     * @return
     */
    public String validateUpdateBooking(UpdateBookingDto updateBookingDto, Booking booking){
        boolean timeInFuture =  updateBookingDto.getRequestTime().toLocalDateTime().isAfter(new Timestamp(System.currentTimeMillis()).toLocalDateTime());

//        booking requested to update should exist in system
        if(booking==null){
            return Message.error.BOOKING_NOT_FOUND_BY_CODE;

        }
//        canceled or completed booking cannot be updated
        else if(booking.getStatus().equals(BookingStatus.CANCELLED.toString()) ||
                booking.getStatus().equals(BookingStatus.COMPLETED.toString()) ){
            return Message.error.CANNOT_UPDATE_CANCEL_COMPLETE_BOOKING;
        }
//        updated request time should be in future
        else if (!timeInFuture) {
            return Message.error.REQUEST_TIME_IN_PAST;

        }
        return Message.success.BOOKING_DTO_VALIDATED;

    }

    /**
     * To validate rating request params
     * @param ratingDto
     * @param booking
     * @return
     */
    public String validateRateBooking(RatingDto ratingDto, Booking booking){
//        booking requested to rate should exist in system
        if(booking==null){
            return Message.error.BOOKING_NOT_FOUND_BY_CODE;
        }
//        only completed booking can be rated
        else if(!booking.getStatus().equals(BookingStatus.COMPLETED.toString())){
            return Message.error.BOOKING_NOT_COMPLETED;
        }
//        validate rating value should be between defined constants
        else if (!Util.inRange(ratingDto.getRating(), Constant.RATING_MIN, Constant.RATING_MAX)) {
            return String.format(Message.error.INVALID_RATING,Constant.RATING_MIN,Constant.RATING_MAX);
        }
        return Message.success.BOOKING_DTO_VALIDATED;
    }

    /**
     * Validate change booking request of cancel and completed status update
     * @param booking
     * @param response
     * @param bookingStatus
     * @return
     */
    public NetworkResponseDto changeBookingStatusValidation(Booking booking, NetworkResponseDto response, BookingStatus bookingStatus){
        response.setCode(HttpStatus.BAD_REQUEST.value());
//        booking requested to update should exist in system
        if(booking==null){
            response.setMsg(Message.error.BOOKING_NOT_FOUND_BY_CODE);
            return response;
        }
//          when update status to completed or canceled case
        if(bookingStatus.equals(BookingStatus.CANCELLED) ||
                bookingStatus.equals(BookingStatus.COMPLETED)){
            // ignore if its already canceled or completed
            if(booking.getStatus().equals(BookingStatus.CANCELLED.toString()) ||
                    booking.getStatus().equals(BookingStatus.COMPLETED.toString()) ){
                response.setMsg(Message.error.CANNOT_UPDATE_CANCEL_COMPLETE_BOOKING);
                return response;

            }
            else if(booking.getStatus().equals(BookingStatus.REQUESTED.toString())){
                response.setMsg(Message.error.REQUESTED_BOOKING_NOT_COMPLETE);
                return response;
            }
        }
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    public NetworkResponseDto confirmBookingValidation(Booking booking, NetworkResponseDto response){
        response.setCode(HttpStatus.BAD_REQUEST.value());
        if(booking==null){
            response.setMsg(Message.error.BOOKING_NOT_FOUND_BY_CODE);
            return response;
        } else if (!booking.getStatus().equals(BookingStatus.REQUESTED.toString())) {
            response.setMsg(String.format(Message.error.BOOKING_IS_ALREADY_,booking.getStatus()));
            return response;
        }
        response.setCode(HttpStatus.OK.value());
        return response;
    }
}
