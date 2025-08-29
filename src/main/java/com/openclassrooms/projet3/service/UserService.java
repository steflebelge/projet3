package com.openclassrooms.projet3.service;

import com.openclassrooms.projet3.model.UserModel;
import com.openclassrooms.projet3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
//import jakarta.annotation.PostConstruct;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authorities;
    }

    public Optional<UserModel> getUser(final Long id) {
        return userRepository.findById(id);
        //??si user non trouvé ?
    }


//    @PostConstruct
//    public void testConnexion() {
//        UserModel user = new UserModel();
//        user.setName("test");
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String hashedPassword = encoder.encode("test");
//        user.setPassword(hashedPassword );
//        user.setEmail("test@test.fr");
//        userRepository.save(user);

//        long count = userRepository.count();
//        System.out.println("Nombre d'utilisateurs en base : " + count);
//    }
}
