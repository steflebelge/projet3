package com.openclassrooms.projet3.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO (Data Transfer Object) utilisé pour valider les données envoyées
 * par le client lors de la connexion d'un utilisateur via l'API.
 */
@Data
public class LoginUserDtoValidation {

    /**
     * Adresse email de l'utilisateur.
     * Ne peut pas être vide et doit être un email valide.
     */
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    /**
     * Mot de passe de l'utilisateur.
     * Ne peut pas être vide et doit contenir entre 6 et 100 caractères.
     */
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, max = 100, message = "Le mot de passe doit faire au moins 6 caractères")
    private String password;
}
