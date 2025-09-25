package com.openclassrooms.projet3.repository;

import com.openclassrooms.projet3.model.MessageModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository Spring Data JPA pour gérer l'entité MessageModel.
 *
 * Fournit les opérations CRUD (Create, Read, Update, Delete) sur la table "messages"
 * sans avoir besoin d'implémenter manuellement les méthodes.
 *
 * Étend CrudRepository avec :
 *   - MessageModel : le type d'entité gérée
 *   - Long : le type de la clé primaire de l'entité
 */
@Repository
public interface MessageRepository extends CrudRepository<MessageModel, Long> {
}
