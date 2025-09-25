package com.openclassrooms.projet3.dto;

import lombok.Data;

/**
 * DTO (Data Transfer Object) utilisé pour renvoyer les informations d'un message
 * après sa création via l'API.
 */
@Data
public class CreateMessageDtoResponse {

    /** Identifiant unique du message dans la base de données */
    private Long id;

    /** Identifiant de la location associée à ce message */
    private Integer rental_id;

    /** Identifiant de l'utilisateur ayant créé ce message */
    private Integer user_id;

    /** Contenu textuel du message */
    private String message;

    /** Date de création du message */
    private String created_at;

    /** Date de dernière mise à jour du message */
    private String updated_at;
}
