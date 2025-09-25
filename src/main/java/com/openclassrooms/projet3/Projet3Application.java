package com.openclassrooms.projet3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale de l'application Spring Boot "Projet3".
 *
 * Cette classe sert de point d'entrée à l'application et démarre le contexte
 * Spring, initialisant tous les beans, les configurations et le serveur web embarqué.
 */
@SpringBootApplication
public class Projet3Application {

	/**
	 * Point d'entrée de l'application.
	 *
	 * @param args arguments passés depuis la ligne de commande (optionnels)
	 */
	public static void main(String[] args) {
		SpringApplication.run(Projet3Application.class, args);
	}
}

