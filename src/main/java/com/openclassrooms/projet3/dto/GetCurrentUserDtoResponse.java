package com.openclassrooms.projet3.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class GetCurrentUserDtoResponse {

    private Long id;

    private String email;

    private String name;

    private String password;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}
