package fr.cmp.kyrios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.cmp.kyrios.model.ProfilSIModel;

@Repository
public interface ProfilSIRepository extends JpaRepository<ProfilSIModel, Integer> {
    boolean existsByName(String name);
}
