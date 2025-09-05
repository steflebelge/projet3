package com.openclassrooms.projet3.controlleur;

import com.openclassrooms.projet3.dto.getCurrentUserDto;
import com.openclassrooms.projet3.dto.registerUserDto;
import com.openclassrooms.projet3.model.UserModel;
import com.openclassrooms.projet3.repository.UserRepository;
import com.openclassrooms.projet3.service.UserService;
import com.openclassrooms.projet3.services.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    public JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    public AuthController(JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Post - register
     * @return Token ou 400
     */
    @PostMapping("/auth/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid registerUserDto userRegisterDto){
        // Vérifier si l'utilisateur existe déjà
        if (userService.findByName(userRegisterDto.getName()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Un utilisateur avec ce nom existe déjà");
        }
        if (userService.findByEmail(userRegisterDto.getEmail()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Un utilisateur avec cet email existe déjà");
        }

        // Création de l'utilisateur
        UserModel user = new UserModel();
        user.setEmail(userRegisterDto.getEmail());
        user.setName(userRegisterDto.getName());
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword())); // Hash du mot de passe
        user.setCreated_at(new Timestamp(System.currentTimeMillis()));
        user.setUpdated_at(new Timestamp(System.currentTimeMillis()));
        userService.save(user);

        String token = jwtService.generateJwtTokenForUser(user);

        return ResponseEntity.ok(token);
    }

    /**
     * Post - Login via basic auth
     *
     * @return le token ou 401
     * testé et OK
     */
    @PostMapping("/auth/login")
    public ResponseEntity<String> getToken(Authentication authentication) {
        String token = jwtService.generateJwtToken(authentication);
        return ResponseEntity.ok(token);
    }

    /**
     * GET - recupere les information de l'utilisateur courant
     *
     * @param jwt
     * @return un user model
     */
    @GetMapping("/auth/me")
    public ResponseEntity<getCurrentUserDto> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        String id = jwt.getClaimAsString("uid");
        Optional<UserModel> userOpt = userRepository.findById(Long.valueOf(id));

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
