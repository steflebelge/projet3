package com.openclassrooms.projet3.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class GetUserByIdDtoResponse {

    private Long id;

    private String email;

    private String name;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}
