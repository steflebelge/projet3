package com.openclassrooms.projet3.repository;

import com.openclassrooms.projet3.model.UserModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository Spring Data JPA pour gérer l'entité UserModel.
 *
 * Fournit les opérations CRUD (Create, Read, Update, Delete) sur la table "users"
 * ainsi que des méthodes personnalisées pour rechercher des utilisateurs par nom ou email.
 *
 * Étend CrudRepository avec :
 *   - UserModel : le type d'entité gérée
 *   - Long : le type de la clé primaire de l'entité
 */
@Repository
public interface UserRepository extends CrudRepository<UserModel, Long> {

    /**
     * Recherche un utilisateur unique par son nom.
     *
     * @param name le nom recherché
     * @return un Optional contenant l'utilisateur si trouvé, sinon vide
     */
    Optional<UserModel> findByName(String name);

    /**
     * Recherche un utilisateur unique par son email.
     *
     * @param email l'email recherché
     * @return un Optional contenant l'utilisateur si trouvé, sinon vide
     */
    Optional<UserModel> findByEmail(String email);
}
