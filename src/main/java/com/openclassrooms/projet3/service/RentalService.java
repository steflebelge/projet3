package com.openclassrooms.projet3.service;

import com.openclassrooms.projet3.dto.UpdateRentalDtoValidation;
import com.openclassrooms.projet3.model.RentalModel;
import com.openclassrooms.projet3.repository.RentalRepository;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * Service Spring pour gérer la logique métier liée aux locations (rentals).
 *
 * Ce service utilise le RentalRepository pour effectuer les opérations CRUD
 * sur les locations et encapsule la logique métier associée.
 */
@Data
@Service
public class RentalService {

    /** Repository JPA pour accéder aux locations dans la base de données */
    @Autowired
    RentalRepository rentalRepository;

    /**
     * Récupère une location spécifique par son identifiant.
     *
     * @param id identifiant de la location
     * @return Optional contenant la location si trouvée, sinon vide
     */
    public Optional<RentalModel> getRental(final Long id) {
        return rentalRepository.findById(id);
    }

    /**
     * Récupère toutes les locations.
     *
     * @return un Iterable contenant toutes les locations
     */
    public Iterable<RentalModel> getRentals() {
        return rentalRepository.findAll();
    }

    /**
     * Sauvegarde une nouvelle location dans la base de données.
     *
     * @param rental l'objet RentalModel à sauvegarder
     * @return le RentalModel sauvegardé, avec l'ID généré et les timestamps éventuellement mis à jour
     */
    public RentalModel saveRental(RentalModel rental) {
        RentalModel savedRental = rentalRepository.save(rental);
        return savedRental;
    }

    /**
     * Met à jour une location existante en appliquant uniquement les champs non nuls
     * du DTO de mise à jour.
     *
     * @param updateRentalDtoValidation DTO contenant les champs à mettre à jour (peut être partiel)
     * @param existingRental objet RentalModel existant à mettre à jour
     * @return le RentalModel mis à jour et sauvegardé
     */
    public RentalModel update(UpdateRentalDtoValidation updateRentalDtoValidation, RentalModel existingRental) {

        // Mise à jour manuelle uniquement des champs non nuls du DTO
        if (updateRentalDtoValidation.getName() != null)
            existingRental.setName(updateRentalDtoValidation.getName());
        if (updateRentalDtoValidation.getSurface() != null)
            existingRental.setSurface(updateRentalDtoValidation.getSurface());
        if (updateRentalDtoValidation.getPrice() != null)
            existingRental.setPrice(updateRentalDtoValidation.getPrice());
        if (updateRentalDtoValidation.getDescription() != null)
            existingRental.setDescription(updateRentalDtoValidation.getDescription());

        // Mettre à jour la date de modification
        existingRental.setUpdated_at(new Timestamp(System.currentTimeMillis()));

        // Sauvegarde dans la base et retourne le résultat
        return rentalRepository.save(existingRental);
    }

    /**
     * Supprime une location de la base de données.
     *
     * @param rental la location à supprimer
     */
    public void deleteRental(RentalModel rental) {
        rentalRepository.delete(rental);
    }
}

