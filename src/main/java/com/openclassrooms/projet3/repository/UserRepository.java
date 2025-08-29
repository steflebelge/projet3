package com.openclassrooms.projet3.repository;

import com.openclassrooms.projet3.model.UserModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<UserModel, Long> {
    List<UserModel> findAllByName(String name);
}
