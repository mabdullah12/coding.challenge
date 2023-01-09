package com.abdullah.coding.challenge.commons;

public class Message {

    public static class error{
        public static final String CUSTOMER_NOT_FOUND = "Customer not found";
        public static final String VEHICLE_TYPE_INVALID = "Vehicle type is invalid";
        public static final String CAB_NOT_AVAILABLE = "Cab not available of given vehicle type";
        public static final String REQUEST_TIME_IN_PAST = "Request time should be in future";
        public static final String BOOKING_IS_ALREADY_ = "Booking is already %s";
        public static final String BOOKING_NOT_FOUND_BY_CODE = "Booking not found by booking code";
        public static final String BOOKING_NOT_COMPLETED = "Only completed booking can be rated.";
        public static final String INVALID_RATING = "Rating range should be between %.1f and %.1f .  ";
        public static final String REQUESTED_BOOKING_NOT_COMPLETE = "Only Assigned booking can be completed";

        public static final String CANNOT_UPDATE_CANCEL_COMPLETE_BOOKING = "Completed or Canceled booking cannot be updated";

    }
    public static class success{
        public static final String BOOKING_DTO_VALIDATED = "VALIDATED";
        public static final String BOOKING_CREATED = "Booking created successfully";
        public static final String BOOKING_COMPLETED = "Booking is now completed.";
        public static final String BOOKING_CANCELED = "Booking is canceled.";
        public static final String BOOKING_CONFIRMED = "Booking is confirmed.";
        public static final String BOOKING_UPDATED = "Booking is updated.";
    }
}
