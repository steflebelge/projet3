package com.openclassrooms.projet3.controlleur;

import com.openclassrooms.projet3.dto.GetCurrentUserDtoResponse;
import com.openclassrooms.projet3.dto.LoginUserDtoValidation;
import com.openclassrooms.projet3.dto.RegisterUserDtoValidation;
import com.openclassrooms.projet3.model.UserModel;
import com.openclassrooms.projet3.service.UserService;
import com.openclassrooms.projet3.services.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Operation;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
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
    @PostMapping("/register")
    @Operation(summary = "Endpoint public", security = {})
    public ResponseEntity<Object> registerUser(@RequestBody @Valid RegisterUserDtoValidation registerUserDtoValidation){
        // Vérifier si l'utilisateur existe déjà
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
        userService.save(user);

        Map<String, Object> json = new HashMap<>();
        json.put("token", jwtService.generateJwtTokenForUser(user));

        return ResponseEntity.ok(json);
    }

    /**
     * Post - Login via basic auth
     *
     * @return le token ou 401
     */
    @PostMapping("/login")
    @Operation(summary = "Endpoint public", security = {})
    public ResponseEntity<Object> getToken(@RequestBody @Valid LoginUserDtoValidation loginUserDtoValidation) {
        Optional<UserModel> userOpt = userService.findByEmail(loginUserDtoValidation.getEmail());
        if(userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserModel user = userOpt.get();

        if(!passwordEncoder.matches(loginUserDtoValidation.getPassword(), user.getPassword())) {
            return ResponseEntity.notFound().build();
        }

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
    public ResponseEntity<GetCurrentUserDtoResponse> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        String id = jwt.getClaimAsString("uid");
        Optional<UserModel> userOpt = userService.getUser(Long.valueOf(id));

        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserModel user = userOpt.get();

        // Transformation manuelle vers le DTO
        GetCurrentUserDtoResponse getCurrentUserDtoResponse = new GetCurrentUserDtoResponse();
        getCurrentUserDtoResponse.setId(user.getId());
        getCurrentUserDtoResponse.setEmail(user.getEmail());
        getCurrentUserDtoResponse.setName(user.getName());
        getCurrentUserDtoResponse.setPassword(user.getPassword());
        getCurrentUserDtoResponse.setCreatedAt(user.getCreated_at());
        getCurrentUserDtoResponse.setUpdatedAt(user.getUpdated_at());

        return ResponseEntity.ok(getCurrentUserDtoResponse);
    }
}
