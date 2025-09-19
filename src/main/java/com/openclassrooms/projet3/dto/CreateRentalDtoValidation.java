package com.openclassrooms.projet3.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class CreateRentalDtoValidation {

    @Size(max = 255, message = "Le nom ne peut pas dépasser 255 caractères")
    private String name;

    @DecimalMin(value = "0.0", inclusive = false, message = "La surface doit être positive")
    private BigDecimal surface;

    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être positif")
    private BigDecimal price;

    private MultipartFile picture;

    @Size(max = 2000, message = "La description ne peut pas dépasser 2000 caractères")
    private String description;

    private Integer ownerId;
}
