package fr.cmp.kyrios.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cmp.kyrios.model.App.AppModel;
import fr.cmp.kyrios.model.App.dto.AppDTOCreate;
import fr.cmp.kyrios.model.App.dto.AppDTOResponse;
import fr.cmp.kyrios.repository.AppRepository;
import fr.cmp.kyrios.util.EntityFinder;

@Service
public class AppService {
    @Autowired
    private AppRepository appRepository;

    @Autowired
    private EntityFinder entityFinder;

    public List<AppModel> listAll() {
        return appRepository.findAll();
    }

    public AppModel getById(int id) {
        return entityFinder.findApplicationOrThrow(id);
    }

    @Transactional
    public AppModel create(AppDTOCreate dto) {
        if (appRepository.findByName(dto.getName()).isPresent()) {
            throw new IllegalArgumentException("Une application avec le nom '" + dto.getName() + "' existe déjà");
        }
        AppModel app = new AppModel();
        app.setName(dto.getName());
        app.setDirection(entityFinder.findDirectionOrThrow(dto.getDirectionId()));
        app.setDescription(dto.getDescription());
        app.setDateCreated(LocalDateTime.now());
        return appRepository.save(app);
    }

    @Transactional
    public AppModel update(int id, AppDTOCreate dto) {
        AppModel app = getById(id);
        if (!app.getName().equals(dto.getName()) && appRepository.findByName(dto.getName()).isPresent()) {
            throw new IllegalArgumentException("Une application avec le nom '" + dto.getName() + "' existe déjà");
        }
        app.setName(dto.getName());
        app.setDirection(entityFinder.findDirectionOrThrow(dto.getDirectionId()));
        app.setDescription(dto.getDescription());
        return appRepository.save(app);
    }

    @Transactional
    public void delete(int id) {
        AppModel app = getById(id);
        appRepository.delete(app);
    }

    public AppDTOResponse toDTO(AppModel app) {
        return AppDTOResponse.builder()
                .id(app.getId())
                .name(app.getName())
                .direction(app.getDirection() != null ? app.getDirection().getName() : null)
                .description(app.getDescription())
                .dateCreated(app.getDateCreated())
                .build();
    }
}
