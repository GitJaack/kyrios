package fr.cmp.kyrios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.cmp.kyrios.model.Emploi.DomaineModel;

@Repository
public interface DomaineRepository extends JpaRepository<DomaineModel, Integer> {
    Optional<DomaineModel> findByName(String name);

    Optional<DomaineModel> findById(String id);
}
