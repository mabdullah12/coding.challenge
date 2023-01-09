package com.abdullah.coding.challenge.commons;

import com.abdullah.coding.challenge.entities.Booking;

import java.util.StringJoiner;

public class Util {

    /*
    To generate new Booking code based on combination
    of Booking info
     */
    public static String generateBookingCode(Booking booking){
        String customerName = booking.getCustomer().getName();
        String vehicleType = booking.getVehicleType();
        String createdTime = booking.getCreationTime().toLocalDateTime().toString();

        StringJoiner code = new StringJoiner("-");
        code.add(customerName);
        code.add(vehicleType);
        code.add(createdTime);

        return code.toString();

    }
    public static boolean inRange(double value, double min, double max) {
        return value > min && value < max;
    }


}
