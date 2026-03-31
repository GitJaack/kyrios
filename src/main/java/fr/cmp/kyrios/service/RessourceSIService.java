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
    private RessourceSIDao ressourceSIDao;

    @Autowired
    private CategorieSIDao categorieSIDao;

    @Autowired
    private ReferenceDao referenceDao;

    public List<RessourceSIDTO> listAll() {
        return ressourceSIDao.findAll().stream()
                .map(RessourceSIMapper::toDto)
                .toList();
    }

    public RessourceSIDTO getById(int id) {
        RessourceSIDao.RessourceReadRow row = ressourceSIDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ressource SI avec l'ID " + id + " non trouvee"));
        return RessourceSIMapper.toDto(row);
    }

    public List<RessourceSIDTO> getRessourcesParDefautByDirection(int directionId) {
        if (!referenceDao.existsDirectionById(directionId)) {
            throw new IllegalArgumentException("Direction avec l'ID " + directionId + " non trouvee");
        }
        return ressourceSIDao.findDefaultByDirectionId(directionId).stream()
                .map(RessourceSIMapper::toDto)
                .toList();
    }

    public List<RessourceSIDTO> getRessourcesByCategorie(int id) {
        if (categorieSIDao.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Categorie avec l'ID " + id + " non trouvee");
        }
        return ressourceSIDao.findByCategorieId(id).stream()
                .map(RessourceSIMapper::toDto)
                .toList();
    }

    @Transactional
    public RessourceSIDTO create(RessourceSIDTOCreate dto) {
        if (ressourceSIDao.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Une ressource avec le nom '" + dto.getName() + "' existe déjà");
        }

        if (categorieSIDao.findById(dto.getCategorie()).isEmpty()) {
            throw new IllegalArgumentException("Categorie avec l'ID " + dto.getCategorie() + " non trouvee");
        }

        int ressourceId = ressourceSIDao.insert(dto.getCategorie(), dto.getName(),
                dto.getTypeAcces().name());
        return getById(ressourceId);
    }

    @Transactional
    public void delete(int id) {
        if (ressourceSIDao.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Ressource SI avec l'ID " + id + " non trouvee");
        }
        ressourceSIDao.deleteDefaultLinksByRessourceId(id);
        ressourceSIDao.deleteProfilLinksByRessourceId(id);
        ressourceSIDao.deleteById(id);
    }

}
