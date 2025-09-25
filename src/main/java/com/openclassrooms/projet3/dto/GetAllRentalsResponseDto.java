package com.openclassrooms.projet3.dto;

import java.util.List;

/**
 * DTO (Data Transfer Object) utilisé pour renvoyer la liste de toutes les locations
 * via l'API.
 */
public class GetAllRentalsResponseDto {

    /**
     * Constructeur
     * @param rentals liste des locations à inclure dans la réponse
     */
    public GetAllRentalsResponseDto(List<GetRentalByIdDtoResponse> rentals) {
        this.rentals = rentals;
    }

    /** Liste des locations, chaque élément est un DTO détaillant une location */
    private List<GetRentalByIdDtoResponse> rentals;

    /**
     * Getter pour la liste des locations
     * @return liste de DTOs de locations
     */
    public List<GetRentalByIdDtoResponse> getRentals() {
        return rentals;
    }

    /**
     * Setter pour la liste des locations
     * @param rentals nouvelle liste de DTOs de locations
     */
    public void setRentals(List<GetRentalByIdDtoResponse> rentals) {
        this.rentals = rentals;
    }
}