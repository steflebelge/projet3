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

@Component
public class BasicAuthProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public BasicAuthProvider(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {

        String login = auth.getName();
        String rawPassword = String.valueOf(auth.getCredentials());

        List<UserModel> candidates = userRepository.findAllByName(login);

        UserModel user = candidates.stream()
                .filter(u -> passwordEncoder.matches(rawPassword, u.getPassword()))
                .findFirst()
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));


        PrincipalView principal = new PrincipalView(user.getId(), user.getEmail(), user.getName());

         List<GrantedAuthority> authorities = Collections.emptyList();
        var authToken = new UsernamePasswordAuthenticationToken(principal, null, authorities);
        return authToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
