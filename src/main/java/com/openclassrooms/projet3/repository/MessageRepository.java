package com.openclassrooms.projet3.repository;

import com.openclassrooms.projet3.model.MessageModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<MessageModel, Long> {
}
