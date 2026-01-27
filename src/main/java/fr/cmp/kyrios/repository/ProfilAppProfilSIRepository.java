package fr.cmp.kyrios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.cmp.kyrios.model.App.ProfilAppProfilSI;

@Repository
public interface ProfilAppProfilSIRepository extends JpaRepository<ProfilAppProfilSI, Integer> {
    boolean existsByApplicationIdAndProfilSIId(int applicationId, int profilSIId);
}
