package com.openclassrooms.projet3.service;

import com.openclassrooms.projet3.model.UserModel;
import com.openclassrooms.projet3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service Spring pour gérer la logique métier liée aux utilisateurs.
 * Ce service utilise le UserRepository pour effectuer les opérations CRUD
 * sur les utilisateurs et encapsule la logique métier associée.
 */
@Service
public class UserService {

    /**
     * Repository JPA pour accéder aux utilisateurs dans la base de données
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Récupère un utilisateur par son identifiant.
     *
     * @param id identifiant de l'utilisateur
     * @return Optional contenant l'utilisateur si trouvé, sinon vide
     * Remarque : si l'utilisateur n'est pas trouvé, l'Optional sera vide.
     */
    public Optional<UserModel> getUser(final Long id) {
        return userRepository.findById(id);
    }

    /**
     * Recherche un utilisateur par son nom.
     *
     * @param name nom de l'utilisateur
     * @return Optional contenant l'utilisateur si trouvé, sinon vide
     */
    public Optional<UserModel> findByName(String name) {
        return userRepository.findByName(name);
    }

    /**
     * Recherche un utilisateur par son email.
     *
     * @param email email de l'utilisateur
     * @return Optional contenant l'utilisateur si trouvé, sinon vide
     */
    public Optional<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Sauvegarde un utilisateur dans la base de données.
     *
     * @param user l'objet UserModel à sauvegarder
     * @return le UserModel sauvegardé
     */
    public UserModel save(UserModel user) {
        return userRepository.save(user);
    }
}
