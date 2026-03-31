package fr.cmp.kyrios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cmp.kyrios.dao.ReferenceDao;
import fr.cmp.kyrios.dao.RessourceAppDao;
import fr.cmp.kyrios.mapper.RessourceAppMapper;
import fr.cmp.kyrios.model.App.dto.RessourceAppCategoryDTO;
import fr.cmp.kyrios.model.App.dto.RessourceAppDTOCreate;
import fr.cmp.kyrios.model.App.dto.RessourceAppDTOResponse;
import fr.cmp.kyrios.model.App.dto.RessourceAppDTOUpdate;

@Service
public class RessourceAppService {
    @Autowired
    private ReferenceDao referenceDao;

    @Autowired
    private RessourceAppDao ressourceAppDao;

    public List<RessourceAppDTOResponse> listAll() {
        return ressourceAppDao.findAll().stream()
                .map(RessourceAppMapper::toDto)
                .toList();
    }

    public RessourceAppDTOResponse getById(int id) {
        RessourceAppDao.RessourceAppReadRow row = ressourceAppDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ressource App avec l'ID " + id + " non trouvee"));
        return RessourceAppMapper.toDto(row);
    }

    public List<RessourceAppDTOResponse> getRessourcesByApp(int id) {
        if (!referenceDao.existsApplicationById(id)) {
            throw new IllegalArgumentException("Application avec l'ID " + id + " non trouvee");
        }
        return ressourceAppDao.findByApplicationId(id).stream()
                .map(RessourceAppMapper::toDto)
                .toList();
    }

    public List<RessourceAppCategoryDTO> getRessourcesByCategorie(int applicationId) {
        if (!referenceDao.existsApplicationById(applicationId)) {
            throw new IllegalArgumentException("Application avec l'ID " + applicationId + " non trouvee");
        }

        var rows = ressourceAppDao.findByCategoryByApplicationId(applicationId);
        var categories = new java.util.LinkedHashMap<Integer, RessourceAppCategoryDTO>();

        for (RessourceAppDao.RessourceAppCategoryRow row : rows) {
            RessourceAppCategoryDTO category = categories.computeIfAbsent(row.categoryId(),
                    id -> RessourceAppCategoryDTO.builder()
                            .id(row.categoryId())
                            .name(row.categoryName())
                            .resources(new java.util.ArrayList<>())
                            .build());

            if (row.ressourceId() != null) {
                category.getResources().add(RessourceAppCategoryDTO.ResourceItem.builder()
                        .id(row.ressourceId())
                        .name(row.ressourceName())
                        .description(row.ressourceDescription())
                        .build());
            }
        }

        return new java.util.ArrayList<>(categories.values());
    }

    @Transactional
    public RessourceAppDTOResponse create(RessourceAppDTOCreate dto) {
        if (ressourceAppDao.existsByNameAndApplicationId(dto.getName(), dto.getApplicationId())) {
            throw new IllegalArgumentException("Une ressource d'application avec le nom '" + dto.getName()
                    + "' existe déjà dans cette application.");
        }

        if (!referenceDao.existsApplicationById(dto.getApplicationId())) {
            throw new IllegalArgumentException(
                    "Application avec l'ID " + dto.getApplicationId() + " non trouvee");
        }

        if (dto.getCategoryId() != null) {
            ReferenceDao.RessourceAppCategorieRef category = referenceDao
                    .findRessourceAppCategorieById(dto.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Categorie avec l'ID " + dto.getCategoryId() + " non trouvee"));
            if (category.applicationId() != dto.getApplicationId()) {
                throw new IllegalArgumentException(
                        "La categorie '" + category.name() + "' n'appartient pas a l'application selectionnee");
            }
        }

        int ressourceId = ressourceAppDao.insert(dto.getName(), dto.getDescription(),
                dto.getCategoryId(), dto.getApplicationId());
        return getById(ressourceId);
    }

    @Transactional
    public RessourceAppDTOResponse update(int id, RessourceAppDTOUpdate dto) {
        RessourceAppDao.RessourceAppReadRow ressourceApp = ressourceAppDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ressource App avec l'ID " + id + " non trouvee"));

        if (!ressourceApp.name().equals(dto.getName())
                && ressourceAppDao.existsByNameAndApplicationIdExcludingId(
                        dto.getName(),
                        ressourceApp.applicationId(),
                        id)) {
            throw new IllegalArgumentException("Une ressource d'application avec le nom '" + dto.getName()
                    + "' existe déjà dans cette application.");
        }

        if (dto.getCategoryId() != null) {
            ReferenceDao.RessourceAppCategorieRef category = referenceDao
                    .findRessourceAppCategorieById(dto.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Categorie avec l'ID " + dto.getCategoryId() + " non trouvee"));
            if (category.applicationId() != ressourceApp.applicationId()) {
                throw new IllegalArgumentException(
                        "La categorie '" + category.name() + "' n'appartient pas a l'application de cette ressource");
            }
        }

        ressourceAppDao.update(id, dto.getName(), dto.getDescription(), dto.getCategoryId());

        return getById(id);
    }

    @Transactional
    public void delete(int id) {
        if (ressourceAppDao.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Ressource App avec l'ID " + id + " non trouvee");
        }
        ressourceAppDao.deleteById(id);
    }

}
