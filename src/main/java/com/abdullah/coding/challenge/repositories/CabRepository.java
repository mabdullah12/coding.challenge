package com.abdullah.coding.challenge.repositories;

import com.abdullah.coding.challenge.entities.Cab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface CabRepository extends JpaRepository<Cab, Integer> {

    List<Cab> findCabByStatusAndVehicleType(String status, String vehicleType);
}
