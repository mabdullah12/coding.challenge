package com.abdullah.coding.challenge.services;

import com.abdullah.coding.challenge.Response;
import com.abdullah.coding.challenge.dto.CabDTO;
import com.abdullah.coding.challenge.entities.Cab;
import com.abdullah.coding.challenge.enums.CabStatus;
import com.abdullah.coding.challenge.repository.CabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CabService {
    @Autowired
    private CabRepository repository;

    public Response<?> makeCabAvailable(int cabId) {
        try {

            Optional<Cab> cab = repository.findById(cabId);
            if (cab.isEmpty()) {
                return new Response<>(HttpStatus.NOT_FOUND, "No such cab found", null);
            }

            Cab cabUpdate = cab.get();
            cabUpdate.setStatus(CabStatus.AVAILABLE.toString());
            repository.save(cabUpdate);

            return new Response<>(HttpStatus.NO_CONTENT, "Success", null);

        } catch (Exception ex) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred", null);
        }
    }

    public Response<?> makeCabUnavailable(int cabId) {
        try {

            Optional<Cab> cab = repository.findById(cabId);
            if (cab.isEmpty()) {
                return new Response<>(HttpStatus.NOT_FOUND, "No such cab found", null);
            }

            Cab cabUpdate = cab.get();
            cabUpdate.setStatus(CabStatus.NOT_AVAILABLE.toString());
            repository.save(cabUpdate);

            return new Response<>(HttpStatus.NO_CONTENT, "Success", null);


        } catch (Exception ex) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred", null);
        }
    }

    public Response<Cab> getAvailableCab(String cabType) {
        try {

            Cab cab = repository.getCab(cabType, CabStatus.AVAILABLE.toString());
            if (cab == null) {
                return new Response<>(HttpStatus.NOT_FOUND, "No such cab available", null);
            }

            return new Response<>(HttpStatus.OK, "Success", cab);

        } catch (Exception ex) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred", null);
        }
    }

    public Response<?> setCabBlockStatus(CabDTO cabDTO) {
        try {

            Optional<Cab> cab = repository.findById(cabDTO.getCabId());
            if (cab.isEmpty()) {
                return new Response<>(HttpStatus.NOT_FOUND, "No such cab available", null);
            }

            Cab cabUpdate = cab.get();

            String responseMsg;
            if (cabDTO.isBlocked()) {
                cabUpdate.setStatus(CabStatus.NOT_AVAILABLE.toString());
                cabUpdate.setBlocked(1);
                responseMsg = "Cab Blocked!";
            } else {
                cabUpdate.setStatus(CabStatus.AVAILABLE.toString());
                cabUpdate.setBlocked(0);
                responseMsg = "Cab Unblocked!";
            }

            repository.save(cabUpdate);

            return new Response<>(HttpStatus.OK, responseMsg, null);

        } catch (Exception ex) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred", null);
        }
    }

    public boolean setCabRating(double actualRating, int cabId) {
        Optional<Cab> cab = repository.findById(cabId);
        if (cab.isEmpty()) {
            return false;
        }
        Cab cabUpdate = cab.get();
        cabUpdate.setRating(actualRating);
        repository.save(cabUpdate);

        return true;
    }
}
