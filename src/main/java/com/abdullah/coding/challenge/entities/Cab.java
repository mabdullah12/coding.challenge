package com.abdullah.coding.challenge.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "cab")
public class Cab {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(name = "id")
    Integer id;
    @Column(name = "rating")
    Double rating;
    @Column(name = "vehicle_type")
    String vehicleType;
    @Column(name = "status")
    String status;
}
