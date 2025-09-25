package com.openclassrooms.projet3.auth;

import com.openclassrooms.projet3.model.UserModel;
import com.openclassrooms.projet3.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

// Fournisseur d’authentification personnalisé
@Component
public class BasicAuthProvider implements AuthenticationProvider {

    // Injection et initialisation des dépendances neccessaires
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public BasicAuthProvider(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Méthode de vérification des informations de Login
    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {

        // Récupération des infos fournies par l'utilisateur (email et mot de passe)
        String login = auth.getName();
        String rawPassword = String.valueOf(auth.getCredentials());

        // Recherche un utilisateur avec cet email en base
        Optional<UserModel> candidat = userRepository.findByName(login);
        if (candidat.isEmpty()) {
            //Si il n'y en a pas, on retourne une erreur
            throw new BadCredentialsException("Invalid username or password");
        }else if (!passwordEncoder.matches(rawPassword, candidat.get().getPassword())) {
            // On vérifie si le mot de passe renseigné corresponds au hash en base
            // Si non, on retourne une erreur de mauvais identifiants
            throw new BadCredentialsException("Invalid username or password");
        }

        //On recupere le UserModel correspondant
        UserModel user = candidat.get();

        // Création d'un objet représentant l'utilisateur authentifié via son id, mail et name
        PrincipalView principal = new PrincipalView(user.getId(), user.getEmail(), user.getName());

        // Définitions des roles de l'utilisateur, dans le cas de cette application aucun n'est neccessaire
         List<GrantedAuthority> authorities = Collections.emptyList();

         // Créer et retourne un token d'authentification valide qui sera intégré au headers de chaque requetes
        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }

    // Spécifie la classe de token que le provider peux gérer
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
