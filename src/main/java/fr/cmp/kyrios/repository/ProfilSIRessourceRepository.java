package fr.cmp.kyrios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.cmp.kyrios.model.Si.ProfilSIModel;
import fr.cmp.kyrios.model.Si.ProfilSIRessource;

@Repository
public interface ProfilSIRessourceRepository extends JpaRepository<ProfilSIRessource, Integer> {
    List<ProfilSIRessource> findByProfilSI(ProfilSIModel profilSI);

    void deleteByProfilSI(ProfilSIModel profilSI);
}
