package com.openclassrooms.projet3.services;

import com.openclassrooms.projet3.model.UserModel;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;


/**
 * Service Spring pour la génération de JWT (JSON Web Tokens).
 * Ce service encapsule la logique de création de tokens JWT pour un utilisateur
 * ou pour un utilisateur authentifié, en utilisant un JwtEncoder fourni par Spring Security.
 */
@Service
public class JwtService {

    /**
     * Encodeur JWT utilisé pour générer les tokens
     */
    private final JwtEncoder jwtEncoder;

    /**
     * Constructeur avec injection du JwtEncoder.
     *
     * @param jwtEncoder encodeur JWT fourni par Spring Security
     */
    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * Génère un JWT directement à partir d'un objet UserModel.
     * Utile pour créer un token après l'inscription ou la connexion d'un utilisateur.
     *
     * @param user utilisateur pour lequel générer le token
     * @return token JWT sous forme de chaîne
     */
    public String generateJwtTokenForUser(UserModel user) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(user.getEmail())
                .claim("uid", String.valueOf(user.getId()))
                .claim("name", user.getName())
                .build();

        JwtEncoderParameters jwtEncoderParameters =
                JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);

        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }
}
