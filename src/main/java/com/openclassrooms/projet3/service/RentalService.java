package com.openclassrooms.projet3.service;

import com.openclassrooms.projet3.model.RentalModel;
import com.openclassrooms.projet3.repository.RentalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Data
@Service
public class RentalService {

    @Autowired
    RentalRepository rentalRepository;

    public Optional<RentalModel> getRental(final Long id) {
        return rentalRepository.findById(id);
    }

    public Iterable<RentalModel> getRentals() {
        return rentalRepository.findAll();
    }

    public RentalModel saveRental(RentalModel rental) {
        RentalModel savedRental = rentalRepository.save(rental);
        return savedRental;
    }

    public RentalModel update(Long id, RentalModel newRental) {

        RentalModel existingRental = rentalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location non trouvée."));

        existingRental.setName(newRental.getName());
        existingRental.setDescription(newRental.getDescription());
        existingRental.setPrice(newRental.getPrice());
        existingRental.setOwner_id(newRental.getOwner_id());
        existingRental.setSurface(newRental.getSurface());
        existingRental.setPicture(newRental.getPicture());

        return rentalRepository.save(existingRental);
    }


    public void deleteRental(Long id) {
        rentalRepository.deleteById(id);
    }
}
