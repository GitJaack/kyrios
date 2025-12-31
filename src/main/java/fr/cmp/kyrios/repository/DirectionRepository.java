package fr.cmp.kyrios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.cmp.kyrios.model.DirectionModel;

@Repository
public interface DirectionRepository extends JpaRepository<DirectionModel, Integer> {
    Optional<DirectionModel> findByName(String name);
}
