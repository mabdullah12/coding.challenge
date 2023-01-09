package com.abdullah.coding.challenge.dtos;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UpdateBookingDto {

    String bookingCode;
    Timestamp requestTime;
}
