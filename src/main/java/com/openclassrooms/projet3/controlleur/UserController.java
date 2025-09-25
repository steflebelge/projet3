package com.openclassrooms.projet3.controlleur;

import com.openclassrooms.projet3.dto.GetUserByIdDtoResponse;
import com.openclassrooms.projet3.model.UserModel;
import com.openclassrooms.projet3.service.UserService;
import com.openclassrooms.projet3.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// Définition d'un controlleur REST pour la gestion de l'utilisateur
// Gère le endpoint GET '/api/user/{id}'
@RestController
public class UserController {

    // Injection des dépendances néccessaires
    @Autowired
    private UserService userService;

    /**
     * Déclaration de la route de récupération des infos d'un utilisateur
     * Read - Get a specific user from id
     *
     * @return A GetUserByIdDtoResponse of the user object
     */
    @GetMapping("/api/user/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        // On essaye de récuperer l'utilisateur a partir de l'id fournit
        Optional<UserModel> userOpt = userService.getUser(id);

        // Si il n'existe pas on renvoi une erreur
        if (userOpt.isEmpty()) {
            String res = "Aucun utilisateur avec cet identifiant n'a été trouvé.";
            Map<String, Object> json = new HashMap<>();
            json.put("message", res);
            return ResponseEntity.badRequest().body(json);
        }

        // Sinon on récupère le userModel correspondant
        UserModel user = userOpt.get();

        // Transformation manuelle vers le DTO
        GetUserByIdDtoResponse getUserByIdDtoResponse = new GetUserByIdDtoResponse();
        getUserByIdDtoResponse.setId(user.getId());
        getUserByIdDtoResponse.setEmail(user.getEmail());
        getUserByIdDtoResponse.setName(user.getName());
        getUserByIdDtoResponse.setCreatedAt(DateUtils.formatTimestamp(user.getCreated_at()));
        getUserByIdDtoResponse.setUpdatedAt(DateUtils.formatTimestamp(user.getUpdated_at()));

        //renvoi de la réponse
        return ResponseEntity.ok(getUserByIdDtoResponse);
    }

}
