package com.openclassrooms.projet3.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * DTO (Data Transfer Object) utilisé pour valider les données envoyées
 * par le client lors de la création d'un nouveau message via l'API.
 */
@Data
public class CreateMessageDtoValidation {

    /**
     * Identifiant de la location associée au message.
     * Ne peut pas être nul.
     */
    @NotNull(message = "Le rental_id ne peut pas être nul")
    private Integer rental_id;

    /**
     * Identifiant de l'utilisateur créant le message.
     * Ne peut pas être nul.
     */
    @NotNull(message = "Le user_id ne peut pas être nul")
    private Integer user_id;

    /**
     * Contenu du message.
     * Ne peut pas être vide et ne doit pas dépasser 500 caractères.
     */
    @NotBlank(message = "Le message ne peut pas être vide")
    @Size(max = 500, message = "Le message ne peut pas dépasser 500 caractères")
    private String message;
}

