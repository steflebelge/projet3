package com.openclassrooms.projet3.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Configuration Spring pour permettre un chemin d'accès aux fichiers statiques d'images
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Fonction qui permet de définir un chemin de ressources statique personnalisé
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Définition du chemin physique des uploads
        String uploadPath = System.getProperty("user.dir") + "/uploads/";

        // Mapping des ressources vers le dossier uploads
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath);
    }
}
