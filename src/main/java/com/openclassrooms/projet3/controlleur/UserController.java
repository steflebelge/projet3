package com.openclassrooms.projet3.controlleur;

import com.openclassrooms.projet3.dto.getCurrentUserDto;
import com.openclassrooms.projet3.model.UserModel;
import com.openclassrooms.projet3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
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
     * @return - An object of User type
     * testé et OK
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<getCurrentUserDto> getUserById(@PathVariable Long id){
        Optional<UserModel> userOpt = userService.getUser(Long.valueOf(id));

        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserModel user = userOpt.get();

        // Transformation manuelle vers le DTO
        getCurrentUserDto dto = new getCurrentUserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setPassword(user.getPassword());
        dto.setCreatedAt(user.getCreated_at());
        dto.setUpdatedAt(user.getUpdated_at());

        return ResponseEntity.ok(dto);
    }

}
