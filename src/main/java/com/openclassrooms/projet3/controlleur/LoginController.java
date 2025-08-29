package com.openclassrooms.projet3.controlleur;

import com.openclassrooms.projet3.services.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    public JwtService jwtService;

    public LoginController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public String getToken(Authentication authentication) {
        System.out.println("fonction gettoke,n");
        String token = jwtService.generateJwtToken(authentication);
        return token;
    }
}
