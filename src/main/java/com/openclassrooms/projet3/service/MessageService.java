package com.openclassrooms.projet3.service;

import com.openclassrooms.projet3.model.MessageModel;
import com.openclassrooms.projet3.repository.MessageRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service Spring pour gérer la logique métier liée aux messages.
 *
 * Ce service utilise le MessageRepository pour effectuer les opérations CRUD
 * sur les messages et encapsule la logique métier associée.
 */
@Data
@Service
public class MessageService {

    /** Repository JPA pour accéder aux messages dans la base de données */
    @Autowired
    private MessageRepository messageRepository;

    /**
     * Sauvegarde un message dans la base de données.
     *
     * @param message l'objet MessageModel à sauvegarder
     * @return le MessageModel sauvegardé, avec l'ID généré et les timestamps éventuellement mis à jour
     */
    public MessageModel saveMessage(MessageModel message) {
        return messageRepository.save(message);
    }
}
