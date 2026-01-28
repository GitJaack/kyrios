package fr.cmp.kyrios.repository.App;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.cmp.kyrios.model.App.ProfilAppRessource;

@Repository
public interface ProfilAppRessourceRepository extends JpaRepository<ProfilAppRessource, Integer> {
}
