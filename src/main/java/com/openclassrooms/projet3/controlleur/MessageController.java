package com.openclassrooms.projet3.controlleur;

import com.openclassrooms.projet3.dto.CreateMessageDtoResponse;
import com.openclassrooms.projet3.dto.CreateMessageDtoValidation;
import com.openclassrooms.projet3.dto.CreateRentalDtoResponse;
import com.openclassrooms.projet3.model.MessageModel;
import com.openclassrooms.projet3.service.MessageService;
import com.openclassrooms.projet3.utils.DateUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

// Définition d'un controlleur REST pour la gestion des messages
// Gère le endpoint POST '/api/messages'
@RestController
public class MessageController {

    // Injection des dépendances néccessaires
    @Autowired
    private MessageService messageService;


    /**
     * Déclaration de la route d'envoi d'un nouveau message
     * POST - Add a new message
     * @param createMessageDtoValidation A CreateMessageDtoValidation object
     * @return A CreateMessageDtoResponse of the new message object
     */
    @PostMapping("/api/messages")
    public ResponseEntity<Object>  createMessage(
            @RequestBody @Valid CreateMessageDtoValidation createMessageDtoValidation,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            String res = "Une erreur a été detectée lors de la validation des informations renseignées.";
            Map<String, Object> json = new HashMap<>();
            json.put("message", res);
            return ResponseEntity.badRequest().body(json);
        }


        // Création de l'objet MessageModel a partir du DTO recu
        MessageModel newMessage = new MessageModel();
        newMessage.setMessage(createMessageDtoValidation.getMessage());
        newMessage.setRental_id(createMessageDtoValidation.getRental_id());
        newMessage.setUser_id(createMessageDtoValidation.getUser_id());
        newMessage.setCreated_at(new Timestamp(System.currentTimeMillis()));
        newMessage.setUpdated_at(new Timestamp(System.currentTimeMillis()));

        // Enregistrement en base du message
        MessageModel savedMessage = messageService.saveMessage(newMessage);

        // Préparation de la réponse via DTO
        CreateMessageDtoResponse createMessageDtoResponse = new CreateMessageDtoResponse();
        createMessageDtoResponse.setMessage(savedMessage.getMessage());
        createMessageDtoResponse.setRental_id(savedMessage.getRental_id());
        createMessageDtoResponse.setUser_id(savedMessage.getUser_id());
        createMessageDtoResponse.setCreated_at(DateUtils.formatTimestamp(savedMessage.getCreated_at()));
        createMessageDtoResponse.setUpdated_at(DateUtils.formatTimestamp(savedMessage.getUpdated_at()));

        // Renvoi de la réponse
        String res = "Message created successfully";
        Map<String, Object> json = new HashMap<>();
        json.put("message", res);
        return ResponseEntity.ok(json);
    }
}
