package com.openclassrooms.projet3.controlleur;

import com.openclassrooms.projet3.dto.CreateMessageDtoResponse;
import com.openclassrooms.projet3.dto.CreateMessageDtoValidation;
import com.openclassrooms.projet3.dto.CreateRentalDtoResponse;
import com.openclassrooms.projet3.model.MessageModel;
import com.openclassrooms.projet3.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;


    /**
     * Create - Add a new message
     * @param createMessageDtoValidation A CreateMessageDtoValidation object
     * @return A CreateMessageDtoResponse of the new message object
     */
    @PostMapping("/api/messages")
    public ResponseEntity<CreateMessageDtoResponse>  createMessage(@RequestBody @Valid CreateMessageDtoValidation createMessageDtoValidation){
        MessageModel newMessage = new MessageModel();
        newMessage.setMessage(createMessageDtoValidation.getMessage());
        newMessage.setRental_id(createMessageDtoValidation.getRental_id());
        newMessage.setUser_id(createMessageDtoValidation.getUser_id());
        newMessage.setCreated_at(new Timestamp(System.currentTimeMillis()));
        newMessage.setUpdated_at(new Timestamp(System.currentTimeMillis()));

        MessageModel savedMessage = messageService.saveMessage(newMessage);

        CreateMessageDtoResponse createMessageDtoResponse = new CreateMessageDtoResponse();
        createMessageDtoResponse.setMessage(savedMessage.getMessage());
        createMessageDtoResponse.setRental_id(savedMessage.getRental_id());
        createMessageDtoResponse.setUser_id(savedMessage.getUser_id());
        createMessageDtoResponse.setCreated_at(savedMessage.getCreated_at());
        createMessageDtoResponse.setUpdated_at(savedMessage.getUpdated_at());

        return ResponseEntity.ok(createMessageDtoResponse);
    }
}
