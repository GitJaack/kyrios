package fr.cmp.kyrios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.cmp.kyrios.model.Si.ProfilSIModel;

@Repository
public interface ProfilSIRepository extends JpaRepository<ProfilSIModel, Integer> {
    Optional<ProfilSIModel> findByName(String name);
}
