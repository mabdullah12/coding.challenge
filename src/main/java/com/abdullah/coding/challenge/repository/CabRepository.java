package com.abdullah.coding.challenge.repository;

import com.abdullah.coding.challenge.entities.Cab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CabRepository extends JpaRepository<Cab, Integer> {
    @Query(value = "SELECT * FROM cab WHERE vehicle_type = :vehicleType AND status = :statusType AND blocked = 0 LIMIT 1", nativeQuery = true)
    Cab getCab(@Param("vehicleType") String vehicleType, @Param("statusType") String statusType);
}
