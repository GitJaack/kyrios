package fr.cmp.kyrios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.cmp.kyrios.model.App.ProfilAppModel;

@Repository
public interface ProfilAppRepository extends JpaRepository<ProfilAppModel, Integer> {

}
