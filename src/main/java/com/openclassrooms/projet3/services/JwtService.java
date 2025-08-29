package com.openclassrooms.projet3.services;

import com.openclassrooms.projet3.auth.PrincipalView;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtService {

    private JwtEncoder jwtEncoder;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateJwtToken(Authentication authentication) {
        PrincipalView princpialView = (PrincipalView) authentication.getPrincipal();

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(princpialView.email())
                .claim("uid", String.valueOf(princpialView.id()))
                .claim("name", princpialView.username())
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);

        // Retour du token sous forme de chaîne
        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }
}
