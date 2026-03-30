package fr.cmp.kyrios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cmp.kyrios.dao.CategorieSIDao;
import fr.cmp.kyrios.dao.ReferenceDao;
import fr.cmp.kyrios.dao.RessourceSIDao;
import fr.cmp.kyrios.mapper.RessourceSIMapper;
import fr.cmp.kyrios.model.Si.dto.ressourceSI.RessourceSIDTO;
import fr.cmp.kyrios.model.Si.dto.ressourceSI.RessourceSIDTOCreate;

@Service
public class RessourceSIService {
    @Autowired
    private RessourceSIDao ressourceSIJdbcRepository;

    @Autowired
    private CategorieSIDao categorieSIJdbcRepository;

    @Autowired
    private ReferenceDao jdbcReferenceRepository;

    public List<RessourceSIDTO> listAll() {
        return ressourceSIJdbcRepository.findAll().stream()
                .map(RessourceSIMapper::toDto)
                .toList();
    }

    public RessourceSIDTO getById(int id) {
        RessourceSIDao.RessourceReadRow row = ressourceSIJdbcRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ressource SI avec l'ID " + id + " non trouvee"));
        return RessourceSIMapper.toDto(row);
    }

    public List<RessourceSIDTO> getRessourcesParDefautByDirectionReadOnlyJdbc(int directionId) {
        if (!jdbcReferenceRepository.existsDirectionById(directionId)) {
            throw new IllegalArgumentException("Direction avec l'ID " + directionId + " non trouvee");
        }
        return ressourceSIJdbcRepository.findDefaultByDirectionId(directionId).stream()
                .map(RessourceSIMapper::toDto)
                .toList();
    }

    public List<RessourceSIDTO> getRessourcesByCategorieReadOnlyJdbc(int id) {
        if (categorieSIJdbcRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Categorie avec l'ID " + id + " non trouvee");
        }
        return ressourceSIJdbcRepository.findByCategorieId(id).stream()
                .map(RessourceSIMapper::toDto)
                .toList();
    }

    @Transactional
    public RessourceSIDTO create(RessourceSIDTOCreate dto) {
        if (ressourceSIJdbcRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Une ressource avec le nom '" + dto.getName() + "' existe déjà");
        }

        if (categorieSIJdbcRepository.findById(dto.getCategorie()).isEmpty()) {
            throw new IllegalArgumentException("Categorie avec l'ID " + dto.getCategorie() + " non trouvee");
        }

        int ressourceId = ressourceSIJdbcRepository.insert(dto.getCategorie(), dto.getName(),
                dto.getTypeAcces().name());
        return getById(ressourceId);
    }

    @Transactional
    public void delete(int id) {
        if (ressourceSIJdbcRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Ressource SI avec l'ID " + id + " non trouvee");
        }
        ressourceSIJdbcRepository.deleteDefaultLinksByRessourceId(id);
        ressourceSIJdbcRepository.deleteProfilLinksByRessourceId(id);
        ressourceSIJdbcRepository.deleteById(id);
    }

}
