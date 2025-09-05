package com.openclassrooms.projet3.controlleur;

import com.openclassrooms.projet3.model.MessageModel;
import com.openclassrooms.projet3.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;


    /**
     * Create - Add a new message
     * @param message A Message  object
     * @return The message object saved
     */
    @PostMapping("/messages")
    public MessageModel createMessage(@RequestBody MessageModel message){
        return messageService.saveMessage(message);
    }
}
