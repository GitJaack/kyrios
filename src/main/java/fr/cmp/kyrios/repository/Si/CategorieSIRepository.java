package fr.cmp.kyrios.repository.Si;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.cmp.kyrios.model.Si.CategorieSIModel;

@Repository
public interface CategorieSIRepository extends JpaRepository<CategorieSIModel, Integer> {
    Optional<CategorieSIModel> findByName(String name);
}
