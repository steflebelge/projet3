package com.openclassrooms.projet3.controlleur;

import com.openclassrooms.projet3.model.UserModel;
import com.openclassrooms.projet3.repository.UserRepository;
import com.openclassrooms.projet3.service.UserService;
import com.openclassrooms.projet3.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;
    public JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Post - Login via basic auth
     * @return le token ou 401
     * testé et OK
     */
    @PostMapping("/auth/login")
    public String getToken(Authentication authentication) {
        String token = jwtService.generateJwtToken(authentication);
        return token;
    }

    /**
     * GET - recupere les information de l'utilisateur courant
     * @param jwt
     * @return un user model
     */
    @GetMapping("/auth/me")
    public Optional<UserModel> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        String id = jwt.getClaimAsString("uid");
        Optional<UserModel> user = userRepository.findById(Long.valueOf(id));
        return user;
    }
}
