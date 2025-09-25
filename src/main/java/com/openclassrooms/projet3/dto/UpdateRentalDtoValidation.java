package com.openclassrooms.projet3.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) utilisé pour valider les données envoyées
 * par le client lors de la mise à jour d'une location via l'API.
 */
@Data
public class UpdateRentalDtoValidation {

    /**
     * Nom de la location.
     * Longueur maximale : 255 caractères.
     */
    @Size(max = 255, message = "Le nom ne peut pas dépasser 255 caractères")
    private String name;

    /**
     * Surface de la location (en m²).
     * Doit être strictement positive.
     */
    @DecimalMin(value = "0.0", inclusive = false, message = "La surface doit être positive")
    private BigDecimal surface;

    /**
     * Prix de la location.
     * Doit être strictement positif.
     */
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être positif")
    private BigDecimal price;

    /**
     * Description textuelle de la location.
     * Longueur maximale : 2000 caractères.
     */
    @Size(max = 2000, message = "La description ne peut pas dépasser 2000 caractères")
    private String description;
}

