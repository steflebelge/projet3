package com.openclassrooms.projet3.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class CreateMessageDtoValidation {

    @NotNull(message = "Le rental_id ne peut pas être nul")
    private Integer rental_id;

    @NotNull(message = "Le user_id ne peut pas être nul")
    private Integer user_id;

    @NotBlank(message = "Le message ne peut pas être vide")
    @Size(max = 500, message = "Le message ne peut pas dépasser 500 caractères")
    private String message;
}
