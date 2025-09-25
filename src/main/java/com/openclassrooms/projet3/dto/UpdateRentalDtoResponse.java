package com.openclassrooms.projet3.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * DTO (Data Transfer Object) utilisé pour renvoyer les informations mises à jour
 * d'une location via l'API.
 */
@Data
public class UpdateRentalDtoResponse {

    /** Identifiant unique de la location dans la base de données */
    private Long id;

    /** Nom de la location */
    private String name;

    /** Surface de la location (en m²), type BigDecimal pour précision */
    private BigDecimal surface;

    /** Prix de la location (en monnaie locale), type BigDecimal pour précision */
    private BigDecimal price;

    /** URL ou chemin de l'image associée à la location */
    private String picture;

    /** Description textuelle de la location */
    private String description;

    /** Identifiant de l'utilisateur propriétaire de la location */
    @JsonProperty("owner_id")
    private Integer ownerId;

    /** Date de création de la location (formatée en String) */
    @JsonProperty("created_at")
    private String createdAt;

    /** Date de dernière mise à jour de la location (formatée en String) */
    @JsonProperty("updated_at")
    private String updatedAt;
}

