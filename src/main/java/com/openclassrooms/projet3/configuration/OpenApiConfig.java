package com.openclassrooms.projet3.configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Fichier de configuration du Swagger
@Configuration
public class OpenApiConfig {

    // Création d'un objet personnalisé pour la documentation de l'API
    // qui sera injecté dans le contexte Spring
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()

                        // Ajout d'un schema de sécurité bearer au format JWT
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )

                // Indique que toutes les requetes sur l'API neccessite le schéma de sécurité declaré juste au dessus "bearerAuth"
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))

                // Définition des infos diverses
                .info(new Info()
                        .title("API Projet 3")
                        .version("1.0.0")
                        .description("Documentation Swagger avec JWT"));
    }
}
