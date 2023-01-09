package com.abdullah.coding.challenge.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(name = "id")
    Integer id;
    @Column(name = "booking_code")
    String bookingCode;
    @Column(name = "status")
    String status;
    @Column(name = "vehicle_type")
    String vehicleType;
    @Column(name = "request_time")
    Timestamp requestTime;
    @Column(name = "creation_time")
    Timestamp creationTime;
    @Column(name = "modified_time")
    Timestamp modifiedTime;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    Customer customer;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cab_id")
    Cab cab;
}
