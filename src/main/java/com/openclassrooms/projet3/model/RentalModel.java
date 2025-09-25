package com.openclassrooms.projet3.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Entité JPA représentant une location (rental) dans la base de données.
 *
 * Cette entité est mappée à la table "rentals" et contient toutes les informations
 * nécessaires pour gérer une location, y compris son propriétaire, ses détails
 * et les timestamps de création et de mise à jour.
 */
@Data
@Entity
@Table(name = "rentals")
public class RentalModel {

    /**
     * Identifiant unique de la location, généré automatiquement par la base de données.
     * Clé primaire.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nom de la location */
    private String name;

    /** Surface de la location (en m²), type BigDecimal pour précision */
    private BigDecimal surface;

    /** Prix de la location, type BigDecimal pour précision */
    private BigDecimal price;

    /** Chemin vers l'image associée à la location */
    private String picture;

    /** Description textuelle de la location */
    private String description;

    /** Identifiant de l'utilisateur propriétaire de la location. */
    private Integer owner_id;

    /** Date de création de la location */
    private Timestamp created_at;

    /** Date de dernière mise à jour de la location */
    private Timestamp updated_at;
}

