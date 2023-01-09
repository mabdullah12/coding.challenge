package com.abdullah.coding.challenge.dtos;

import com.abdullah.coding.challenge.entities.Booking;
import lombok.Data;

@Data
public class NetworkResponseDto {
    private Integer code;
    private String msg;
    private Booking data;

}
