package com.abdullah.coding.challenge.dtos;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class NewBookingDto {

    String vehicleType;
    Timestamp requestTime;
    Integer customerId;
}
