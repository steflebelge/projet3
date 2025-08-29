package com.openclassrooms.projet3.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "rentals")
public class RentalModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //annotattion de clef primaire
    private Long id;

    private String name;

    private BigDecimal surface;

    private BigDecimal price;

    //emplacement fichier
    private String picture;

    private String description;

    //annotation pour clef etrangere vers user_id
    private Integer owner_id;

    private Timestamp created_at;

    private Timestamp updated_at;

}
