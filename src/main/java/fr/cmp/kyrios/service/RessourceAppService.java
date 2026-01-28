package fr.cmp.kyrios.service;

import java.util.List;
import fr.cmp.kyrios.util.EntityFinder;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cmp.kyrios.model.App.RessourceAppModel;
import fr.cmp.kyrios.model.App.dto.RessourceAppDTOCreate;
import fr.cmp.kyrios.model.App.dto.RessourceAppDTOResponse;
import fr.cmp.kyrios.model.App.dto.RessourceAppDTOUpdate;
import fr.cmp.kyrios.repository.App.RessourceAppRepository;

@Service
public class RessourceAppService {
    @Autowired
    private EntityFinder entityFinder;

    @Autowired
    private RessourceAppRepository ressourceAppRepository;

    RessourceAppService(EntityFinder entityFinder) {
        this.entityFinder = entityFinder;
    }

    public List<RessourceAppModel> listAll() {
        return ressourceAppRepository.findAll();
    }

    public RessourceAppModel getById(int id) {
        return entityFinder.findRessourceAppOrThrow(id);
    }

    public List<RessourceAppModel> getRessourcesByApp(int id) {
        entityFinder.findApplicationOrThrow(id);
        return ressourceAppRepository.findByApplicationId(id);
    }

    @Transactional
    public RessourceAppModel create(RessourceAppDTOCreate dto) {
        if (ressourceAppRepository.findByNameAndApplicationId(dto.getName(), dto.getApplicationId()).isPresent()) {
            throw new IllegalArgumentException("Une ressource d'application avec le nom '" + dto.getName()
                    + "' existe déjà dans cette application.");
        }

        RessourceAppModel ressourceApp = new RessourceAppModel();
        ressourceApp.setApplication(entityFinder.findApplicationOrThrow(dto.getApplicationId()));
        ressourceApp.setName(dto.getName());
        ressourceApp.setDescription(dto.getDescription());

        return ressourceAppRepository.save(ressourceApp);
    }

    @Transactional
    public RessourceAppModel update(int id, RessourceAppDTOUpdate dto) {
        RessourceAppModel ressourceApp = getById(id);
        if (!ressourceApp.getName().equals(dto.getName())
                && ressourceAppRepository
                        .findByNameAndApplicationId(dto.getName(), ressourceApp.getApplication().getId())
                        .isPresent()) {
            throw new IllegalArgumentException("Une ressource d'application avec le nom '" + dto.getName()
                    + "' existe déjà dans cette application.");
        }

        ressourceApp.setName(dto.getName());
        ressourceApp.setDescription(dto.getDescription());

        return ressourceAppRepository.save(ressourceApp);
    }

    @Transactional
    public RessourceAppModel delete(int id) {
        RessourceAppModel ressourceApp = getById(id);
        ressourceAppRepository.delete(ressourceApp);
        return ressourceApp;
    }

    public RessourceAppDTOResponse toDTO(RessourceAppModel ressourceApp) {
        return RessourceAppDTOResponse.builder()
                .id(ressourceApp.getId())
                .name(ressourceApp.getName())
                .description(ressourceApp.getDescription())
                .application(ressourceApp.getApplication().getName())
                .build();
    }

    public List<RessourceAppDTOResponse> toDTOList(List<RessourceAppModel> ressourcesApp) {
        return ressourcesApp.stream()
                .map(this::toDTO)
                .toList();
    }
}
