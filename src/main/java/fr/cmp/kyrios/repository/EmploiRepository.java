package fr.cmp.kyrios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.cmp.kyrios.model.Emploi.EmploiModel;

@Repository
public interface EmploiRepository extends JpaRepository<EmploiModel, Integer> {
}
