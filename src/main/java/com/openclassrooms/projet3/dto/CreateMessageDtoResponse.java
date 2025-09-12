package com.openclassrooms.projet3.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class CreateMessageDtoResponse {

    private Long id;
    private Integer rental_id;
    private Integer user_id;
    private String message;
    private Timestamp created_at;
    private Timestamp updated_at;
}
