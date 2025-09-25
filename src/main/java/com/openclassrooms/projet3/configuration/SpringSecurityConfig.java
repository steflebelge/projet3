package com.openclassrooms.projet3.configuration;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.openclassrooms.projet3.auth.BasicAuthProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;
import java.util.List;

// Déclration de la classe qui indique comment la sécurité est gérée au sein de l'API
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    // Injection de la clé secrete a partir d'application.properties
    @Value("${jwt.secret}")
    private String SECRET;

    // Permet d'authentifier les utilisateurs, ici avec deux providers :
    //  - BasicAuth pour le login (email/mot de passe)
    //  - JwtAuthentication pour la gestion des JWT
    @Bean
    public AuthenticationManager authenticationManager(BasicAuthProvider basic, JwtDecoder decoder) {
        var jwtProvider = new org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider(decoder);
        return new org.springframework.security.authentication.ProviderManager(List.of(basic, jwtProvider));
    }

    // Configuration des règles d'accès/filtrage des routes/pages publiques ou neccessitant une authentification
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationManager authManager) throws Exception {
        return http
                .authorizeHttpRequests(auth ->  auth

                        // Déclaration des routes/pages publiques
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/uploads/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Tout le reste neccessite une authentification de l'utilisateur
                        .anyRequest().authenticated()
                )

                // Désactivation CSRF
                .csrf(csrf -> csrf.disable())

                // Désactivation des sessions
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Utilisation du authManager déclaré au dessus
                .authenticationManager(authManager)

                // Indique l'utilistion des JWT présents dans les headers
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }

    // Permet de hash les mots de passe pour les stocker en base
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Encodeur pour créer des JWT signés avec la clé secrète
    @Bean
    public JwtEncoder jwtEncoder() {
        byte[] key = java.util.Base64.getDecoder().decode(SECRET);
        return new NimbusJwtEncoder(new ImmutableSecret<>(key));
    }

    // Décodeur pour valider et lire les JWT
    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] key = java.util.Base64.getDecoder().decode(SECRET);
        SecretKeySpec secretKey = new SecretKeySpec(key, "HmacSHA256");
        var dec = NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
        dec.setJwtValidator(JwtValidators.createDefaultWithIssuer("self"));
        return dec;
    }
}
