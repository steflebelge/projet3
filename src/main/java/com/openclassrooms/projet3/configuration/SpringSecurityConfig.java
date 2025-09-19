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

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Value("${jwt.secret}")
    private String SECRET;

    @Bean
    public AuthenticationManager authenticationManager(BasicAuthProvider basic, JwtDecoder decoder) {
        var jwtProvider = new org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider(decoder);
        return new org.springframework.security.authentication.ProviderManager(List.of(basic, jwtProvider));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationManager authManager) throws Exception {
        return http
                .authorizeHttpRequests(auth ->  auth
                        .requestMatchers("/api/auth/register").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(authManager)
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        byte[] key = java.util.Base64.getDecoder().decode(SECRET);
        return new NimbusJwtEncoder(new ImmutableSecret<>(key));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] key = java.util.Base64.getDecoder().decode(SECRET);
        SecretKeySpec secretKey = new SecretKeySpec(key, "HmacSHA256");
        var dec = NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
        dec.setJwtValidator(JwtValidators.createDefaultWithIssuer("self"));
        return dec;
    }
}
