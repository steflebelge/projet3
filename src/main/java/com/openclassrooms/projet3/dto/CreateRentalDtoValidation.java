package com.openclassrooms.projet3.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) utilisé pour valider les données envoyées
 * par le client lors de la création d'une nouvelle location via l'API.
 */
@Data
public class CreateRentalDtoValidation {

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
     * Image associée à la location.
     * Utilise MultipartFile pour l'upload de fichiers.
     */
    private MultipartFile picture;

    /**
     * Description textuelle de la location.
     * Longueur maximale : 2000 caractères.
     */
    @Size(max = 2000, message = "La description ne peut pas dépasser 2000 caractères")
    private String description;

    /**
     * Identifiant de l'utilisateur propriétaire de la location.
     */
    private Integer ownerId;
}
