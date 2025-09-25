package com.openclassrooms.projet3.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Entité JPA représentant un utilisateur (user) dans la base de données.
 *
 * Cette entité est mappée à la table "users" et contient toutes les informations
 * nécessaires pour gérer un compte utilisateur, y compris ses identifiants et
 * les timestamps de création et de mise à jour.
 */
@Data
@Entity
@Table(name = "users")
public class UserModel {

    /**
     * Identifiant unique de l'utilisateur, généré automatiquement par la base de données.
     * Clé primaire.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Adresse email de l'utilisateur */
    private String email;

    /** Nom de l'utilisateur */
    private String name;

    /** Mot de passe de l'utilisateur (hashé) */
    private String password;

    /** Date de création du compte */
    private Timestamp created_at;

    /** Date de dernière mise à jour du compte */
    private Timestamp updated_at;
}
