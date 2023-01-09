package com.abdullah.coding.challenge.services;


import com.abdullah.coding.challenge.entities.Cab;
import com.abdullah.coding.challenge.enums.CabStatus;
import com.abdullah.coding.challenge.enums.VehicleType;
import com.abdullah.coding.challenge.repositories.CabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CabService {

    @Autowired
    private CabRepository cabRepository;

    public Cab save(Cab cab){
        return cabRepository.save(cab);
    }
    public Cab updateCabStatus(Cab cab, CabStatus status){
        cab.setStatus(status.toString());
        return cabRepository.save(cab);
    }

    public List<Cab> findAvailableCabByVehicleType(VehicleType vehicleType){
        return cabRepository.findCabByStatusAndVehicleType(CabStatus.AVAILABLE.toString(),vehicleType.name());
    }
    public Optional<Cab> findById(Integer cabId){
        return cabRepository.findById(cabId);
    }

}
