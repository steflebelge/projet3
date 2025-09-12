package com.openclassrooms.projet3.controlleur;

import com.openclassrooms.projet3.dto.GetUserByIdDtoResponse;
import com.openclassrooms.projet3.model.UserModel;
import com.openclassrooms.projet3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Read - Get a specific user from id
     * @return A GetUserByIdDtoResponse of the user object
     */
    @GetMapping("/api/users/{id}")
    public ResponseEntity<GetUserByIdDtoResponse> getUserById(@PathVariable Long id){
        Optional<UserModel> userOpt = userService.getUser(Long.valueOf(id));

        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserModel user = userOpt.get();

        // Transformation manuelle vers le DTO
        GetUserByIdDtoResponse getUserByIdDtoResponse = new GetUserByIdDtoResponse();
        getUserByIdDtoResponse.setId(user.getId());
        getUserByIdDtoResponse.setEmail(user.getEmail());
        getUserByIdDtoResponse.setName(user.getName());
        getUserByIdDtoResponse.setCreatedAt(user.getCreated_at());
        getUserByIdDtoResponse.setUpdatedAt(user.getUpdated_at());

        return ResponseEntity.ok(getUserByIdDtoResponse);
    }

}
