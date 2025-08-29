package com.openclassrooms.projet3.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private String password;

    private Timestamp created_at;

    private Timestamp updated_at;
}


//todo
//retour de get / getAll / create => 1 dto/ endpoint
//Rajouter l utilisation du JWT
//changer retour upload -> JSON
//token key -> app.properties
//routes users et messages

//done
//supprimer tables SESSION en base
