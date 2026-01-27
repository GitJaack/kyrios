package fr.cmp.kyrios.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.cmp.kyrios.model.App.ProfilAppModel;

@Repository
public interface ProfilAppRepository extends JpaRepository<ProfilAppModel, Integer> {
    Optional<ProfilAppModel> findByName(String name);

    Optional<ProfilAppModel> findByNameAndApplicationId(String name, int applicationId);

    List<ProfilAppModel> findByApplicationId(int applicationId);
}
