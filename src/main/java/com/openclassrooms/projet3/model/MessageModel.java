package com.openclassrooms.projet3.model;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;

/**
 * Entité JPA représentant un message dans la base de données.
 *
 * Cette entité est mappée à la table "messages" et contient toutes les
 * informations nécessaires pour gérer les messages associés aux locations
 * et aux utilisateurs.
 */
@Data
@Entity
@Table(name = "messages")
public class MessageModel {

    /** Identifiant unique du message, généré automatiquement par la base */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Identifiant de la location à laquelle ce message est associé */
    private Integer rental_id;

    /** Identifiant de l'utilisateur qui a créé ce message */
    private Integer user_id;

    /** Contenu textuel du message */
    private String message;

    /** Date de création du message */
    private Timestamp created_at;

    /** Date de dernière mise à jour du message */
    private Timestamp updated_at;
}

