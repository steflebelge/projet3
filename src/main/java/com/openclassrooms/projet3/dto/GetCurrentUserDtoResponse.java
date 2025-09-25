package com.openclassrooms.projet3.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * DTO (Data Transfer Object) utilisé pour renvoyer les informations de l'utilisateur
 * actuellement connecté via l'API.
 */
@Data
public class GetCurrentUserDtoResponse {

    /** Identifiant unique de l'utilisateur dans la base de données */
    private Long id;

    /** Adresse email de l'utilisateur */
    private String email;

    /** Nom de l'utilisateur */
    private String name;

    /** Date de création du compte (formatée en String) */
    @JsonProperty("created_at")
    private String createdAt;

    /** Date de dernière mise à jour du compte (formatée en String) */
    @JsonProperty("updated_at")
    private String updatedAt;
}
