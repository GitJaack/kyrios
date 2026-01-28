package fr.cmp.kyrios.repository.App;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.cmp.kyrios.model.App.RessourceAppModel;

@Repository
public interface RessourceAppRepository extends JpaRepository<RessourceAppModel, Integer> {
    Optional<RessourceAppModel> findByName(String name);

    Optional<RessourceAppModel> findByNameAndApplicationId(String name, int applicationId);

    List<RessourceAppModel> findByApplicationId(int id);
}
