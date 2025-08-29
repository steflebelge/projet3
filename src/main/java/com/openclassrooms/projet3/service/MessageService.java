package com.openclassrooms.projet3.service;

import com.openclassrooms.projet3.model.MessageModel;
import com.openclassrooms.projet3.repository.MessageRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public MessageModel saveMessage(MessageModel message) {
        MessageModel savedMessage = messageRepository.save(message);
        return savedMessage;
    }
}
