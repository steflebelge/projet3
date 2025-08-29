package com.openclassrooms.projet3.repository;

import com.openclassrooms.projet3.model.RentalModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends CrudRepository<RentalModel, Long> {
}
