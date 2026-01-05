package fr.cmp.kyrios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.cmp.kyrios.model.Emploi.EmploiModel;

@Repository
public interface EmploiRepository extends JpaRepository<EmploiModel, Integer> {
    Optional<EmploiModel> findById(int id);

    boolean existsById(int id);
}
