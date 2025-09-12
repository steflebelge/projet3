package com.openclassrooms.projet3.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class CreateRentalDtoResponse {

    private Long id;

    private String name;

    private BigDecimal surface;

    private BigDecimal price;

    private String picture;

    private String description;

    private Integer ownerId;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}
