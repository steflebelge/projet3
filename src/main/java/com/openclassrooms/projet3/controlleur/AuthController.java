package com.openclassrooms.projet3.controlleur;

import com.openclassrooms.projet3.dto.GetCurrentUserDtoResponse;
import com.openclassrooms.projet3.dto.LoginUserDtoValidation;
import com.openclassrooms.projet3.dto.RegisterUserDtoValidation;
import com.openclassrooms.projet3.model.UserModel;
import com.openclassrooms.projet3.service.UserService;
import com.openclassrooms.projet3.services.JwtService;
import com.openclassrooms.projet3.utils.DateUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// Définition d'un controlleur REST pour la gestion de l'authentification et de l'inscription des utilisateurs
// Gère tout les endpoints commencant par '/api/auth'
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    @Autowired
    public JwtService jwtService;
    // Injection et initalisation des dépendances néccessaires
    @Autowired
    private UserService userService;

    public AuthController(JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Déclaration de la route d'inscription
     * Post - register
     *
     * @return Token ou 400
     */
    @PostMapping("/register")
    @Operation(summary = "Endpoint public", security = {})
    public ResponseEntity<Object> registerUser(
            @RequestBody @Valid RegisterUserDtoValidation registerUserDtoValidation,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            String res = "Une erreur a été detectée lors de la validation des informations renseignées.";
            Map<String, Object> json = new HashMap<>();
            json.put("message", res);
            return ResponseEntity.badRequest().body(json);
        }

        // Vérifier si l'utilisateur existe déjà
        // Si oui on retourne une erreur avec le message qui va bien
        if (userService.findByName(registerUserDtoValidation.getName()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Un utilisateur avec ce nom existe déjà");
        }
        if (userService.findByEmail(registerUserDtoValidation.getEmail()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Un utilisateur avec cet email existe déjà");
        }

        // Création de l'utilisateur
        UserModel user = new UserModel();
        user.setEmail(registerUserDtoValidation.getEmail());
        user.setName(registerUserDtoValidation.getName());
        user.setPassword(passwordEncoder.encode(registerUserDtoValidation.getPassword()));
        user.setCreated_at(new Timestamp(System.currentTimeMillis()));
        user.setUpdated_at(new Timestamp(System.currentTimeMillis()));

        // Sauvegarde en base
        UserModel savedUser = userService.save(user);

        // Génère un JWT pour l'utilisateur et l'ajoute a la réponse renvoyée au front
        Map<String, Object> json = new HashMap<>();
        json.put("token", jwtService.generateJwtTokenForUser(savedUser));

        return ResponseEntity.ok(json);
    }

    /**
     * Déclaration de la route de connection
     * Post - Login
     *
     * @return le token ou 401
     */
    @PostMapping("/login")
    @Operation(summary = "Endpoint public", security = {})
    public ResponseEntity<Object> getToken(
            @RequestBody @Valid LoginUserDtoValidation loginUserDtoValidation,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            String res = "Une erreur a été detectée lors de la validation des informations renseignées.";
            Map<String, Object> json = new HashMap<>();
            json.put("message", res);
            return ResponseEntity.badRequest().body(json);
        }

        // Recherche de l'utilisateur par son email
        // Si non trouvé, renvoi une erreur
        Optional<UserModel> userOpt = userService.findByEmail(loginUserDtoValidation.getEmail());
        if (userOpt.isEmpty()) {
            String res = "Mauvais email ou mpt de passe.";
            Map<String, Object> json = new HashMap<>();
            json.put("message", res);
            return ResponseEntity.badRequest().body(json);
        }

        // Récupération de l'utilisateur en base
        UserModel user = userOpt.get();

        // Vérification que le mot de passe rentré corresponds au hash du mot de passe en base
        if (!passwordEncoder.matches(loginUserDtoValidation.getPassword(), user.getPassword())) {
            String res = "Mauvais email ou mpt de passe.";
            Map<String, Object> json = new HashMap<>();
            json.put("message", res);
            return ResponseEntity.badRequest().body(json);
        }

        // Création et ajout du token pour cet utilisateur dans la réponse
        String token = jwtService.generateJwtTokenForUser(user);
        Map<String, Object> json = new HashMap<>();
        json.put("token", token);
        return ResponseEntity.ok(json);
    }

    /**
     * GET - recupere les information de l'utilisateur courant
     *
     * @param jwt
     * @return un user model
     */
    @GetMapping("/me")
    public ResponseEntity<Object> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        //Recherche de l'utilisateur a partir de l'uuid inclut dans son token
        String id = jwt.getClaimAsString("uid");
        Optional<UserModel> userOpt = userService.getUser(Long.valueOf(id));

        // Si il n existe pas on renvoi une réponse
        if (userOpt.isEmpty()) {
            String res = "Une erreur est survenue lors de la récupération de vos informations.";
            Map<String, Object> json = new HashMap<>();
            json.put("message", res);
            return ResponseEntity.badRequest().body(json);
        }

        // Sinon on récupère l'objet utilisateur correspondant
        UserModel user = userOpt.get();

        // Transformation manuelle vers le DTO de réponse
        GetCurrentUserDtoResponse getCurrentUserDtoResponse = new GetCurrentUserDtoResponse();
        getCurrentUserDtoResponse.setId(user.getId());
        getCurrentUserDtoResponse.setEmail(user.getEmail());
        getCurrentUserDtoResponse.setName(user.getName());
        getCurrentUserDtoResponse.setCreatedAt(DateUtils.formatTimestamp(user.getCreated_at()));
        getCurrentUserDtoResponse.setUpdatedAt(DateUtils.formatTimestamp(user.getUpdated_at()));

        // Renvoi des informations
        return ResponseEntity.ok(getCurrentUserDtoResponse);
    }
}
