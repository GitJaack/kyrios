package fr.cmp.kyrios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.cmp.kyrios.model.Si.RessourceSIModel;

@Repository
public interface RessourceSIRepository extends JpaRepository<RessourceSIModel, Integer> {
}
