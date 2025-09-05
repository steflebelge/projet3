package com.openclassrooms.projet3.repository;

import com.openclassrooms.projet3.model.UserModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserModel, Long> {
    List<UserModel> findAllByName(String name);

    Optional<UserModel> findByName(String name);

    Optional<UserModel> findByEmail(String email);
}
