package fr.cmp.kyrios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cmp.kyrios.dao.CategorieSIDao;
import fr.cmp.kyrios.mapper.CategorieSIMapper;
import fr.cmp.kyrios.model.Si.dto.categorieSI.CategorieSIDTOCreate;
import fr.cmp.kyrios.model.Si.dto.categorieSI.CategorieSIDTOResponse;

@Service
public class CategorieSIService {
    @Autowired
    private CategorieSIDao categorieSIDao;

    public List<CategorieSIDTOResponse> listAll() {
        return categorieSIDao.findAll().stream()
                .map(CategorieSIMapper::toDto)
                .toList();
    }

    public CategorieSIDTOResponse getById(int id) {
        CategorieSIDao.CategorieReadRow row = categorieSIDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categorie avec l'ID " + id + " non trouvee"));
        return CategorieSIMapper.toDto(row);
    }

    @Transactional
    public CategorieSIDTOResponse create(CategorieSIDTOCreate dto) {
        if (categorieSIDao.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Une catégorie avec le nom '" + dto.getName() + "' existe déjà");
        }

        int categorieId = categorieSIDao.insert(dto.getName());
        return getById(categorieId);
    }

    @Transactional
    public CategorieSIDTOResponse update(int id, String name) {
        CategorieSIDao.CategorieReadRow categorie = categorieSIDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categorie avec l'ID " + id + " non trouvee"));

        if (!categorie.name().equals(name) && categorieSIDao.existsByNameExcludingId(name, id)) {
            throw new IllegalArgumentException("Une catégorie avec le nom '" + name + "' existe déjà");
        }

        categorieSIDao.updateName(id, name);
        return getById(id);
    }

    @Transactional
    public void delete(int id) {
        if (categorieSIDao.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Categorie avec l'ID " + id + " non trouvee");
        }
        categorieSIDao.deleteDefaultRessourceByCategorieId(id);
        categorieSIDao.deleteProfilRessourceByCategorieId(id);
        categorieSIDao.deleteRessourcesByCategorieId(id);
        categorieSIDao.deleteById(id);
    }

}
