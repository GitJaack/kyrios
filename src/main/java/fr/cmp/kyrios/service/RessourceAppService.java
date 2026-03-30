package fr.cmp.kyrios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cmp.kyrios.dao.ReferenceDao;
import fr.cmp.kyrios.dao.RessourceAppDao;
import fr.cmp.kyrios.mapper.RessourceAppMapper;
import fr.cmp.kyrios.model.App.dto.RessourceAppDTOCreate;
import fr.cmp.kyrios.model.App.dto.RessourceAppDTOResponse;
import fr.cmp.kyrios.model.App.dto.RessourceAppDTOUpdate;

@Service
public class RessourceAppService {
    @Autowired
    private ReferenceDao jdbcReferenceRepository;

    @Autowired
    private RessourceAppDao ressourceAppJdbcRepository;

    public List<RessourceAppDTOResponse> listAll() {
        return ressourceAppJdbcRepository.findAll().stream()
                .map(RessourceAppMapper::toDto)
                .toList();
    }

    public RessourceAppDTOResponse getById(int id) {
        RessourceAppDao.RessourceAppReadRow row = ressourceAppJdbcRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ressource App avec l'ID " + id + " non trouvee"));
        return RessourceAppMapper.toDto(row);
    }

    public List<RessourceAppDTOResponse> getRessourcesByAppReadOnlyJdbc(int id) {
        if (!jdbcReferenceRepository.existsApplicationById(id)) {
            throw new IllegalArgumentException("Application avec l'ID " + id + " non trouvee");
        }
        return ressourceAppJdbcRepository.findByApplicationId(id).stream()
                .map(RessourceAppMapper::toDto)
                .toList();
    }

    @Transactional
    public RessourceAppDTOResponse create(RessourceAppDTOCreate dto) {
        if (ressourceAppJdbcRepository.existsByNameAndApplicationId(dto.getName(), dto.getApplicationId())) {
            throw new IllegalArgumentException("Une ressource d'application avec le nom '" + dto.getName()
                    + "' existe déjà dans cette application.");
        }

        if (!jdbcReferenceRepository.existsApplicationById(dto.getApplicationId())) {
            throw new IllegalArgumentException(
                    "Application avec l'ID " + dto.getApplicationId() + " non trouvee");
        }

        int ressourceId = ressourceAppJdbcRepository.insert(dto.getName(), dto.getDescription(),
                dto.getApplicationId());
        return getById(ressourceId);
    }

    @Transactional
    public RessourceAppDTOResponse update(int id, RessourceAppDTOUpdate dto) {
        RessourceAppDao.RessourceAppReadRow ressourceApp = ressourceAppJdbcRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ressource App avec l'ID " + id + " non trouvee"));

        if (!ressourceApp.name().equals(dto.getName())
                && ressourceAppJdbcRepository.existsByNameAndApplicationIdExcludingId(
                        dto.getName(),
                        ressourceApp.applicationId(),
                        id)) {
            throw new IllegalArgumentException("Une ressource d'application avec le nom '" + dto.getName()
                    + "' existe déjà dans cette application.");
        }

        ressourceAppJdbcRepository.update(id, dto.getName(), dto.getDescription());

        return getById(id);
    }

    @Transactional
    public void delete(int id) {
        if (ressourceAppJdbcRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Ressource App avec l'ID " + id + " non trouvee");
        }
        ressourceAppJdbcRepository.deleteById(id);
    }

}
