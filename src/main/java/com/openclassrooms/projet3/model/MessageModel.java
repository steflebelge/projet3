package com.openclassrooms.projet3.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "messages")
public class MessageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer rental_id;

    private Integer user_id;

    private String message;

    private Timestamp created_at;

    private Timestamp updated_at;
}
