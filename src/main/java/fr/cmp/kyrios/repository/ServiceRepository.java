package fr.cmp.kyrios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.cmp.kyrios.model.Emploi.ServiceModel;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceModel, Integer> {
    Optional<ServiceModel> findByName(String name);

    Optional<ServiceModel> findById(String id);
}
