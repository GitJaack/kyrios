package fr.cmp.kyrios.repository.App;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.cmp.kyrios.model.App.AppModel;

@Repository
public interface AppRepository extends JpaRepository<AppModel, Integer> {
    Optional<AppModel> findByName(String name);
}