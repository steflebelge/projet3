package com.openclassrooms.projet3.repository;

import com.openclassrooms.projet3.model.UserModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserModel, Long> {
    public UserModel findByName(String name);
}
